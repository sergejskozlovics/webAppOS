package org.webappos.server;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.security.Key;
import java.security.KeyPair;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.Security;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.FileUtils;
import org.bouncycastle.cert.X509CertificateHolder;
import org.bouncycastle.cert.jcajce.JcaX509CertificateConverter;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.openssl.PEMKeyPair;
import org.bouncycastle.openssl.PEMParser;
import org.bouncycastle.openssl.jcajce.JcaPEMKeyConverter;
import org.shredzone.acme4j.Authorization;
import org.shredzone.acme4j.Certificate;
import org.shredzone.acme4j.Registration;
import org.shredzone.acme4j.RegistrationBuilder;
import org.shredzone.acme4j.Session;
import org.shredzone.acme4j.Status;
import org.shredzone.acme4j.challenge.Challenge;
import org.shredzone.acme4j.challenge.Http01Challenge;
import org.shredzone.acme4j.exception.AcmeConflictException;
import org.shredzone.acme4j.exception.AcmeException;
import org.shredzone.acme4j.util.CSRBuilder;
import org.shredzone.acme4j.util.CertificateUtils;
import org.shredzone.acme4j.util.KeyPairUtils;
import org.slf4j.LoggerFactory;


public class CertBot4j {
	private static org.slf4j.Logger logger = LoggerFactory.getLogger(CertBot4j.class);
	
	static {
		Security.addProvider(new BouncyCastleProvider());
	}
	
    // File name of the User Key Pair
    private static final String USER_KEY_FILE_NAME = "user.key";

    // File name of the Domain Key Pair
    public static final String DOMAIN_KEY_FILE_NAME = "privkey.pem";//"domain.key";

    // File name of the CSR
    private static final String DOMAIN_CSR_FILE_NAME = "domain.csr";

    // File name of the signed certificate
    public static final String DOMAIN_CHAIN_FILE_NAME = "fullchain.pem";//"domain-chain.crt";   cert+chain

    // RSA key size of generated key pairs
    private static final int KEY_SIZE = 2048;
    
    private static boolean isDomainAccessible(String domain, String webrootFolder) {
    	File f;
    	try {
			f = File.createTempFile("webAppOS-CertBot4j-", ".txt", new File(webrootFolder));
		} catch (IOException e) {
			return false;
		}
    	
    	URL website;
		try {
			website = new URL("http://"+domain+"/"+f.getName());
		} catch (MalformedURLException e) {
			f.delete();
			return false;
		}
		ReadableByteChannel rbc;
    	try {
    		URLConnection conn = website.openConnection();
    		if (conn == null) {
    			f.delete();
    			return false;    			
    		}
    		
    		conn.setReadTimeout(3000);
    		InputStream is = conn.getInputStream();
    		if (is == null) {
    			f.delete();
    			return false;    			
    		}
			rbc = Channels.newChannel(is);
			
		} catch (IOException e) {			
			f.delete();
			return false;
		}
    	
    	try {
			rbc.close();
		} catch (IOException e) {
		}
		f.delete();
    	return true;
    }

    
    private static String getCurrentTimeStamp() {
        SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd-(HH-mm-ss)");
        Date now = new Date();
        String strDate = sdfDate.format(now);
        return strDate;
    }

    
    private  static String dateToString(Date d) {
        SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd");
        String strDate = sdfDate.format(d);
        return strDate;
    }

    private static Date stringToDate(String s) throws ParseException {
        SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd");
		return sdfDate.parse(s);
    }
    
    private static String readFile(String path) 
    		  throws IOException 
    		{
    		  byte[] encoded = Files.readAllBytes(new File(path).toPath());
    		  return new String(encoded, Charset.forName("UTF-8"));
    		}
    
    
    
    // https://stackoverflow.com/questions/9711173/convert-ssl-pem-to-p12-with-or-without-openssl
    public static byte[] convertPEMToPKCS12(final String keyFile, final String cerFile,
            final String password)
            throws IOException, CertificateException, KeyStoreException, NoSuchAlgorithmException
        {
            // Get the private key
            FileReader reader = new FileReader(keyFile);

            PEMParser pem = new PEMParser(reader);
            PEMKeyPair pemKeyPair = ((PEMKeyPair)pem.readObject());
            JcaPEMKeyConverter jcaPEMKeyConverter = new JcaPEMKeyConverter().setProvider("BC");
            KeyPair keyPair = jcaPEMKeyConverter.getKeyPair(pemKeyPair);

            PrivateKey key = keyPair.getPrivate();

            pem.close();
            reader.close();

            // Get the certificate
            reader = new FileReader(cerFile);
            pem = new PEMParser(reader);

            X509CertificateHolder certHolder = (X509CertificateHolder) pem.readObject();
            java.security.cert.Certificate X509Certificate =
                new JcaX509CertificateConverter().setProvider("BC")
                    .getCertificate(certHolder);

            pem.close();
            reader.close();

            // Put them into a PKCS12 keystore and write it to a byte[]
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            KeyStore ks = KeyStore.getInstance("PKCS12");
            ks.load(null);
            ks.setKeyEntry("alias", (Key) key, password.toCharArray(),
                new java.security.cert.Certificate[]{X509Certificate});
            ks.store(bos, password.toCharArray());
            bos.close();
            return bos.toByteArray();
        }

    static {
    	Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
    }
    
    public static KeyStore PEMToKeyStore(final String keyFile, final String cerFile)
            throws IOException, CertificateException, KeyStoreException, NoSuchAlgorithmException
        {
            // Get the private key
            FileReader reader = new FileReader(keyFile);

            PEMParser pem = new PEMParser(reader);
            PEMKeyPair pemKeyPair = ((PEMKeyPair)pem.readObject());
            JcaPEMKeyConverter jcaPEMKeyConverter = new JcaPEMKeyConverter().setProvider("BC");
            KeyPair keyPair = jcaPEMKeyConverter.getKeyPair(pemKeyPair);

            PrivateKey key = keyPair.getPrivate();

            pem.close();
            reader.close();

            // Get the certificate
            reader = new FileReader(cerFile);
            pem = new PEMParser(reader);

            X509CertificateHolder certHolder = (X509CertificateHolder) pem.readObject();
            java.security.cert.Certificate X509Certificate =
                new JcaX509CertificateConverter().setProvider("BC")
                    .getCertificate(certHolder);

            pem.close();
            reader.close();

            KeyStore ks = KeyStore.getInstance("PKCS12");
            ks.load(null);
            ks.setKeyEntry("alias", (Key) key, "".toCharArray(), // ""==password
                new java.security.cert.Certificate[]{X509Certificate});
            
            
            return ks;
        }
    
	private static ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
	
	private static void log(String certsFolder, String s) {
		try {
			FileWriter fw = null;
			try{
				new File(certsFolder+File.separator+"log").mkdirs();
		        fw = new FileWriter(certsFolder+File.separator+"log"+File.separator+"log.txt", true/*append*/);
		        fw.write(new Date().toString()+": ");
		        fw.write(s);
		        fw.write("\n");
			}
			finally {
				if (fw!=null)
					fw.close();
			}
		}
		catch(Throwable t) {			
		}		
	}
		
   	public static void ensureCertificates(String acme_uri, String domain, String certsFolder, String webrootFolder, int renewAfterDays, Runnable onRenew) {   		
   		if (API.config.hasOnlyIP) {
   			logger.info("Skipping ensuring certificates, since only IP is specified.");
   			return;
   		}
   		
   		final Runnable r = new Runnable() {

			@Override
			public void run() {
				try {
					ensureCertificatesOnce(acme_uri, domain, certsFolder, webrootFolder, renewAfterDays, onRenew);
				} catch (IOException | AcmeException e) {
					
					ByteArrayOutputStream baos = new ByteArrayOutputStream();
					PrintStream ps;
					try {
						ps = new PrintStream(baos, true, "utf-8");
					} catch (UnsupportedEncodingException e1) {
						log(certsFolder, e.toString());
						return;
					}
					e.printStackTrace(ps);
					String content = new String(baos.toByteArray(), StandardCharsets.UTF_8);
					ps.close();

					log(certsFolder, content);
					
	   				logger.info("Re-scheduling in "+1+" day as for trying to obtain a new certificate after possible configuration corrections are made...");
					scheduler.schedule(this, 1, TimeUnit.DAYS);
				}
			}
   			
   		};

   		try {
   			String s = readFile(certsFolder+File.separator+"renew.date");
   			Date d = stringToDate(s);
   			
   			long diff = d.getTime() - new Date().getTime();
   			long diffDays = TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
   			
   			if (diffDays>0) {   				
   				logger.info("Scheduling in "+diffDays+" days...");
   				// renew date is in the future, launching scheduler in diffDays...   				
   				scheduler.scheduleAtFixedRate(r, diffDays, renewAfterDays, TimeUnit.DAYS);
   				return;
   			}
   		}
   		catch (Throwable t) {
   			// do nothing...
   		}

		logger.info("Running now, and scheduling every "+renewAfterDays+" days...");
		// launching now and set up scheduler
		r.run();
		scheduler.scheduleAtFixedRate(r, renewAfterDays, renewAfterDays, TimeUnit.DAYS);
   	}
   	
   	private static void ensureCertificatesOnce(String acme_uri, String domain, String certsFolder, String webrootFolder, int renewAfterDays, Runnable onRenew) throws IOException, AcmeException {
   		if (API.config.hasOnlyIP) {
   			logger.info("Skipping ensuring certificates, since only IP is specified.");
   			return;
   		}
   		else   			
   			logger.info("Ensuring certificates...");
   		try {
   			// try to delete previous acme challenges...
   			FileUtils.deleteDirectory(new File(webrootFolder+File.separator+".well-known"+File.separator+"acme-challenge"));
   		}
   		catch (Throwable t) {
   			// do nothing...
   		}   		
   		
   		if (!isDomainAccessible(domain, webrootFolder)) {
   			
			// displaying a smart message:
			String s = "http://"+domain+":80 must be accessible to obtain certificates for secure HTTPS connections.";
			if (API.config.port==80)
				s += " Please, ensure that port 80 is not used and is not blocked by firewalls.";
			else {
				s += " Please, ensure that port 80 is not used, is not blocked by firewalls, and is redirected to port "+API.config.port+".";
				boolean b = isDomainAccessible(domain+":"+API.config.port, webrootFolder);
				if (b)
					s += " Good news is that http://"+domain+":"+API.config.port+" seems to be accessible!";
				else
					s += " Regrettably, http://"+domain+":"+API.config.port+" seems to be inaccessible either!";
			}
			logger.error("Domain http://"+domain+" is not accessible. "+s);

   			throw new AcmeException("Domain is not accessible - "+domain+"; web-root="+webrootFolder+". "+s);
   		}

   		
   		
   		
        // Load the user key file. If there is no key file, create a new one.
        // Keep this key pair in a safe place! In a production environment, you will not be
        // able to access your account again if you should lose the key pair.
        KeyPair userKeyPair = loadOrCreateKeyPair(certsFolder, USER_KEY_FILE_NAME, false);

        Session session = new Session(acme_uri, userKeyPair);
        
        //https://acme-v01.api.letsencrypt.org/directory

        // Get the Registration to the account.
        // If there is no account yet, create a new one.
        Registration reg = findOrRegisterAccount(session);

        // Separately authorize every requested domain.
        authorize(reg, domain, webrootFolder);

        // Load or create a key pair for the domains. This should not be the userKeyPair!
        KeyPair domainKeyPair = loadOrCreateKeyPair(certsFolder, DOMAIN_KEY_FILE_NAME, true); // must create

        // Generate a CSR for all of the domains, and sign it with the domain key pair.
        CSRBuilder csrb = new CSRBuilder();
        csrb.addDomain(domain);
        csrb.sign(domainKeyPair);

        // Write the CSR to a file, for later use.
        try (Writer out = new FileWriter(new File(certsFolder+File.separator+DOMAIN_CSR_FILE_NAME))) {
            csrb.write(out);
        }

        // Now request a signed certificate.
        Certificate certificate = reg.requestCertificate(csrb.getEncoded());

        // Download the leaf certificate and certificate chain.
        //--rem deprecated: X509Certificate cert = certificate.download();
        //--rem deprecated: X509Certificate[] chain = certificate.downloadChain();
        X509Certificate[] chain = certificate.downloadFullChain();

        // Write a combined file containing the certificate and chain.
        try (FileWriter fw = new FileWriter(certsFolder+File.separator+DOMAIN_CHAIN_FILE_NAME)) {
            //--rem deprecated: CertificateUtils.writeX509CertificateChain(fw, cert, chain);
        	CertificateUtils.writeX509Certificates(fw, chain);
        }
        
        // Save current date + renewAfterDays
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        cal.add(Calendar.DATE, renewAfterDays);
        FileWriter fw = new FileWriter(certsFolder+File.separator+"renew.date");
        fw.write(dateToString(cal.getTime()));
        fw.close();
        
        if (onRenew != null) {
        	try {
        		onRenew.run();
        	}
        	catch(Throwable t) {        		
        	}
        }
                
        new File(certsFolder+File.separator+"archive").mkdirs();        
        
        try {
        	Files.copy(new File(certsFolder+File.separator+DOMAIN_CHAIN_FILE_NAME).toPath(), new File(certsFolder+File.separator+"archive"+File.separator+getCurrentTimeStamp()+"-"+DOMAIN_CHAIN_FILE_NAME).toPath());
        }
        catch(Throwable t) {}
        try {
        	Files.copy(new File(certsFolder+File.separator+DOMAIN_KEY_FILE_NAME).toPath(), new File(certsFolder+File.separator+"archive"+File.separator+getCurrentTimeStamp()+"-"+DOMAIN_KEY_FILE_NAME).toPath());
        }
        catch(Throwable t) {}
        
        
/*        try (FileWriter fw = new FileWriter(certsFolder+File.separator+"cert.pem")) {
            CertificateUtils.writeX509Certificate(cert, fw);
        }
        try (FileWriter fw = new FileWriter(certsFolder+File.separator+"chain.pem")) {
            CertificateUtils.writeX509CertificateChain(fw, null, chain);
        }        */

    }

    /**
     * Loads a key pair from specified file. If the file does not exist,
     * a new key pair is generated and saved.
     *
     * @return {@link KeyPair}.
     */
    private static KeyPair loadOrCreateKeyPair(String certsFolder, String fileName, boolean create) throws IOException {
    	File file = new File(certsFolder+File.separator+fileName);
    	if (create)
    		file.delete();
    	
        if (file.exists()) {
            try (FileReader fr = new FileReader(file)) {
                return KeyPairUtils.readKeyPair(fr);
            }
        } else {
            KeyPair domainKeyPair = KeyPairUtils.createKeyPair(KEY_SIZE);
            try (FileWriter fw = new FileWriter(file)) {
                KeyPairUtils.writeKeyPair(domainKeyPair, fw);
            }
            new File(certsFolder+File.separator+"archive").mkdirs();        
            Files.copy(file.toPath(), new File(certsFolder+File.separator+"archive"+File.separator+getCurrentTimeStamp()+"-"+fileName).toPath());
            return domainKeyPair;
        }
    }

    /**
     * Finds your {@link Registration} at the ACME server. It will be found by your user's
     * public key. If your key is not known to the server yet, a new registration will be
     * created.
     * <p>
     * This is a simple way of finding your {@link Registration}. A better way is to get
     * the URL of your new registration with {@link Registration#getLocation()} and store
     * it somewhere. If you need to get access to your account later, reconnect to it via
     * {@link Registration#bind(Session, URL)} by using the stored location.
     *
     * @param session
     *            {@link Session} to bind with
     * @return {@link Registration} connected to your account
     */
    private static Registration findOrRegisterAccount(Session session) throws AcmeException {
        Registration reg;

        try {
            // Try to create a new Registration.
            reg = new RegistrationBuilder().create(session);

            // This is a new account. Let the user accept the Terms of Service.
            // We won't be able to authorize domains until the ToS is accepted.
            URI agreement = reg.getAgreement();
            acceptAgreement(reg, agreement);

        } catch (AcmeConflictException ex) {
            // The Key Pair is already registered. getLocation() contains the
            // URL of the existing registration's location. Bind it to the session.
            reg = Registration.bind(session, ex.getLocation());
        }

        return reg;
    }

    /**
     * Authorize a domain. It will be associated with your account, so you will be able to
     * retrieve a signed certificate for the domain later.
     * <p>
     * You need separate authorizations for subdomains (e.g. "www" subdomain). Wildcard
     * certificates are not currently supported.
     *
     * @param reg
     *            {@link Registration} of your account
     * @param domain
     *            Name of the domain to authorize
     * @throws IOException 
     */
    private static void authorize(Registration reg, String domain, String webrootFolder) throws AcmeException, IOException {
        // Authorize the domain.
        Authorization auth = reg.authorizeDomain(domain);

        // Find the desired challenge and prepare it.
        Challenge challenge = httpChallenge(auth, domain, webrootFolder);

        if (challenge == null) {
            throw new AcmeException("No challenge found.");
        }

        // If the challenge is already verified, there's no need to execute it again.
        if (challenge.getStatus() == Status.VALID) {
            return;
        }

        // Now trigger the challenge.
        challenge.trigger();

        // Poll for the challenge to complete.
        try {
            int attempts = 10;
            while (challenge.getStatus() != Status.VALID && attempts-- > 0) {
                // Did the authorization fail?
                if (challenge.getStatus() == Status.INVALID) {
                    throw new AcmeException("Challenge failed. Giving up. Restart webAppOS to re-attempt.");
                }

                // Wait for a few seconds
                Thread.sleep(3000L);

                // Then update the status
                challenge.update();
            }
        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
        }

        // All reattempts are used up and there is still no valid authorization?
        if (challenge.getStatus() != Status.VALID) {
            throw new AcmeException("Failed to pass the challenge for domain " + domain + ". Giving up. Restart webAppOS to re-attempt.");
        }
    }

    /**
     * Prepares a HTTP challenge.
     * <p>
     * The verification of this challenge expects a file with a certain content to be
     * reachable at a given path under the domain to be tested.
     * <p>
     * This example outputs instructions that need to be executed manually. In a
     * production environment, you would rather generate this file automatically, or maybe
     * use a servlet that returns {@link Http01Challenge#getAuthorization()}.
     *
     * @param auth
     *            {@link Authorization} to find the challenge in
     * @param domain
     *            Domain name to be authorized
     * @return {@link Challenge} to verify
     * @throws IOException 
     */
    public static Challenge httpChallenge(Authorization auth, String domain, String webrootFolder) throws AcmeException, IOException {
        // Find a single http-01 challenge
        Http01Challenge challenge = auth.findChallenge(Http01Challenge.TYPE);
        if (challenge == null) {
            throw new AcmeException("Found no " + Http01Challenge.TYPE + " challenge, don't know what to do...");
        }

        String dir =  webrootFolder+File.separator+".well-known"+File.separator+"acme-challenge";
        new File(dir).mkdirs();
        
        FileWriter fw = new FileWriter(dir+File.separator+challenge.getToken());
        fw.write(challenge.getAuthorization());
        fw.close();

        return challenge;
    }

	
		
    /**
     * Presents the user a link to the Terms of Service, and asks for confirmation. If the
     * user denies confirmation, an exception is thrown.
     *
     * @param reg
     *            {@link Registration} User's registration
     * @param agreement
     *            {@link URI} of the Terms of Service
     */
    public static void acceptAgreement(Registration reg, URI agreement) throws AcmeException {
/*        int option = JOptionPane.showConfirmDialog(null,
                        "Do you accept the Terms of Service?\n\n" + agreement,
                        "Accept ToS",
                        JOptionPane.YES_NO_OPTION);
        if (option == JOptionPane.NO_OPTION) {
            throw new AcmeException("User did not accept Terms of Service");
        }*/

        // Motify the Registration and accept the agreement
        reg.modify().setAgreement(agreement).commit();
    }

	public static void main(String[] args) {

    	String acme_uri = "acme://letsencrypt.org/staging"; 
        Collection<String> domains = Arrays.asList(args);
        try {
            ensureCertificates(acme_uri, "test.webappos.org", "D:\\webAppOS\\etc\\acme\\certs", "D:\\webAppOS\\etc\\acme\\web-root", 60, new Runnable() {

				@Override
				public void run() {
					logger.info("Just renewed certificates...");
				}
            	
            });
        } catch (Exception ex) {
            logger.error("Failed to get a certificate for domains " + domains +". Exception: "+ex);
        }

	}

}
