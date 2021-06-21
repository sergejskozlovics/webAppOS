package org.webappos.scopes.google;

import com.google.api.client.auth.oauth2.BearerToken;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.model.About;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.FileList;
import com.google.gson.JsonElement;
import org.webappos.properties.WebServiceProperties;
import org.webappos.server.API;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GoogleDriveDriver implements org.webappos.fs.IFileSystem {
	Drive service;
	String pathPrefix;
	public GoogleDriveDriver(String login, String path) {
		try {
			this.pathPrefix = path;
			System.out.println("Calling ["+login+"] path=["+path+"]");
			final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
			this.service = new Drive.Builder(HTTP_TRANSPORT, getDefaultJsonFactory(), getCredentials(login))
					.setApplicationName("webappos")
					.build();
		} catch (Throwable throwable){
			throwable.printStackTrace();
		}
	}
	
	private String getFullPath(String path) {
		if (this.pathPrefix!=null && this.pathPrefix.length()>0) {
			if (path!=null && path.length()>0)
				return this.pathPrefix+"/"+path;
			else
				return this.pathPrefix;
		}
		else
			return path;
	}
	

	@Override
	public boolean copyPath(String src, String dst) {
		src = getFullPath(src);
		dst = getFullPath(dst);
		//test with folders
		//both are files or folders
		//rewrite
		try {
			File srcFile = getAsFile(src);
			File dstFile = getAsFile(dst);
			File copy = new File();
			
			copy.setParents(Collections.singletonList(dstFile.getId()));
			copy.setName(srcFile.getName());
			
			service.files().copy(srcFile.getId(), copy).setFields("id, parents").execute();
			return true;
		} catch (Throwable throwable) {
			throwable.printStackTrace();
			return false;
		}
	}

	@Override
	public boolean createDirectory(String path) {
		path = getFullPath(path);
		File fileMetadata = new File();
		fileMetadata.setName(path);
		fileMetadata.setMimeType("application/vnd.google-apps.folder");

		try {
			File file = service.files().create(fileMetadata)
					.setFields("id")
					.execute();
			System.out.println("Folder ID: " + file.getId());
			return true;
		} catch (Throwable throwable){
			throwable.printStackTrace();
			return false;
		}
	}

	@Override
	public boolean deletePath(String path) {
		path = getFullPath(path);
		//test with folder
		try {
			File file = getAsFile(path);
			service.files().delete(file.getId()).execute();
			return true;
		} catch (Throwable throwable) {
			throwable.printStackTrace();
			return false;
		}
	}

	@Override
	public InputStream downloadFile(String path) {
		path = getFullPath(path);
		try{
			System.out.println("Downloading ... " + path);
			
			ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
			File file = getAsFile(path);
			
			if (file.getMimeType().contains("google-apps")){
				String fileType = "unknown";
				switch (file.getMimeType()){
					case "application/vnd.google-apps.document":
						fileType = "application/vnd.oasis.opendocument.text";
						break;
					case "application/vnd.google-apps.spreadsheet":
						fileType = "application/x-vnd.oasis.opendocument.spreadsheet";
						break;
					case "application/vnd.google-apps.drawing":
						String fileExtension = file.getFileExtension();
						
						//is not tested
						switch (fileExtension){
							case "JPEG":
								fileType = "image/jpeg";
								break;
							case "PNG":
								fileType = "image/png";
								break;
							case "SVG":
								fileType = "image/svg+xml";
								break;
							case "PDF":
								fileType = "application/pdf";
								break;
							default:
								System.out.println("ERROR: Unknown photo format!!!");
								break;
						}
						
						break;
					case "application/vnd.google-apps.presentation":
						fileType = "application/vnd.oasis.opendocument.presentation";
						break;
					default:
						System.out.println("ERROR: Unknown format!!!");
						break;
				}
				service.files()
						.export(file.getId(), fileType)
						.executeMediaAndDownloadTo(outputStream);
			} else {
				service.files()
						.get(file.getId())
						.executeMediaAndDownloadTo(outputStream);
			}
			System.out.println("bytes="+outputStream.toByteArray().length);
			
			return new ByteArrayInputStream(outputStream.toByteArray());
		}catch (Throwable throwable){
			throwable.printStackTrace();
			return null;
		}
	}

	@Override
	public boolean pathExists(String path) {
		path = getFullPath(path);
		File file = getAsFile(path);
		return file != null;
	}

	@Override
	public AllocationInfo getAllocationInfo() {
		try {
			AllocationInfo allocationInfo = new AllocationInfo();
			About about = service.about().get().setFields("storageQuota/limit, storageQuota/usage").execute();
			allocationInfo.totalSpace = about.getStorageQuota().getLimit();
			allocationInfo.usedSpace = about.getStorageQuota().getUsage();
			return allocationInfo;
		} catch (Throwable throwable) {
			throwable.printStackTrace();
			return null;
		}
	}

	@Override
	public List<PathInfo> listDirectory(String path) {
		path = this.getFullPath(path);
		double d = Math.random();
		System.out.println("Call list path=["+path+"] rnd="+d);
		List<PathInfo> retVal = new ArrayList<PathInfo>();
		String folderID = "";

		try{
			String pageToken = null;
			do {
				FileList result;
				if (path.equals("")) {
					result = service.files().list()
							.setQ("'root' in parents")
							.setFields("nextPageToken, files(id, name, createdTime, modifiedTime, size, mimeType)")
							.setPageToken(pageToken)
							.execute();
				}
				else {
					folderID = getFolderID(path, service);
					result = service.files().list()
							.setQ("'" + folderID + "' in parents")
							.setFields("nextPageToken, files(id, name, createdTime, modifiedTime, size, mimeType, parents)")
							.setPageToken(pageToken)
							.execute();
				}
				for (File file : result.getFiles()) {
					System.out.println("Found file:	" + file.getName() + " " + file.getId());
					PathInfo pathInfo = new PathInfo();
					pathInfo.name = file.getName();
					pathInfo.created = file.getCreatedTime().getValue();
					pathInfo.modified = file.getModifiedTime().getValue();
					pathInfo.isDirectory = file.getMimeType().equals("application/vnd.google-apps.folder");
					if (!file.getMimeType().contains("google-apps")){
						pathInfo.size = file.getSize();
					} else{
						pathInfo.size = 0;
					}
					retVal.add(pathInfo);
				}
				pageToken = result.getNextPageToken();
			} while (pageToken != null);
			return retVal;
		}catch (Throwable throwable){
			System.out.println("exception for rnd="+d);
			throwable.printStackTrace();
			return null;
		}
	}
	
	@Override
	public PathInfo getPathInfo(String path) {
		if (path == null)
			return null;
		path = getFullPath(path);
		if (path.equals("")) {
			PathInfo pi = new PathInfo();
			pi.created = 0;
			pi.isDirectory = true;
			pi.modified = 0;
			pi.name = "";
			pi.size = 0;
			return pi;
		}
		else {
			try{
				String parentName = new java.io.File(path).getParent();
				String childName =new java.io.File(path).getName();
				String parentId = "root";
				if (parentName != null && parentName.length()>0)
					parentId = getFolderID(parentName, service);
				String pageToken = null;
				do {
					FileList result = service.files().list()
							.setQ("name = '"+childName+"' and '" + parentId + "' in parents")
							.setFields("nextPageToken, files(id, name, createdTime, modifiedTime, parents, size, mimeType)")
							.setPageToken(pageToken)
							.execute();
					for (File file : result.getFiles()) {
						System.out.println("Found file:	[" + file.getName() + "] [" + file.getId() + "] [" + file.getParents() + "]");
						PathInfo pathInfo = new PathInfo();
						pathInfo.name = file.getName();
						pathInfo.created = file.getCreatedTime().getValue();
						pathInfo.modified = file.getModifiedTime().getValue();
						pathInfo.size = 0;
						Long size = file.getSize();
						if (size!=null)
							pathInfo.size = size;
						pathInfo.isDirectory = file.getMimeType().equals("application/vnd.google-apps.folder");
						return pathInfo;
					}
					pageToken = result.getNextPageToken();
				} while (pageToken != null);
				return null;
			}catch (Throwable throwable){
				throwable.printStackTrace();
				return null;
			}
		}
	}
	
	private File getAsFile(String path) {
		if (path == null)
			return null;
		path = getFullPath(path);
		if (path.equals("")) {
			return null;
		}
		else {
			try{
				String parentName = new java.io.File(path).getParent();
				String childName =new java.io.File(path).getName();
				String parentId = "root";
				if (parentName != null && parentName.length()>0)
					parentId = getFolderID(parentName, service);
				String pageToken = null;
				do {
					FileList result = service.files().list()
							.setQ("name = '"+childName+"' and '" + parentId + "' in parents")
							.setFields("nextPageToken, files(id, name, createdTime, webContentLink, webViewLink, modifiedTime, parents, size, mimeType)")
							.setPageToken(pageToken)
							.execute();
					for (File file : result.getFiles()) {
						System.out.println("Found file:	[" + file.getName() + "] [" + file.getId() + "] [" + file.getParents() + "]");
						return file;
					}
					pageToken = result.getNextPageToken();
				} while (pageToken != null);
				return null;
			}catch (Throwable throwable){
				throwable.printStackTrace();
				return null;
			}
		}
	}

	@Override
	public boolean renamePath(String src, String dst) {
		src = getFullPath(src);
		dst = getFullPath(dst);
		try {
			File file = new File();
			File srcFile = getAsFile(src);
			file.setName(getName(dst));
			service.files().update(srcFile.getId(), file).execute();
			return true;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public List<String> searchForFiles(String startDirectory, String query) {
		startDirectory = getFullPath(startDirectory);
		List<PathInfo> list = this.listDirectory(startDirectory);
		if (list == null){
			return null;
		}
		
		List<String> retVal = new ArrayList<String>();
		for (PathInfo pi : list) {
			retVal.add(pi.name);
		}
		return retVal;
	}

	@Override
	public boolean uploadFile(String path, InputStream stream, long length, boolean overwrite) {
		path = getFullPath(path);
		//TODO: upload file
		//docs: https://developers.google.com/drive/api/v3/manage-uploads
		return false;
	}

	private static JsonFactory getDefaultJsonFactory() {
		return JacksonFactory.getDefaultInstance();
	}
	
	public static Credential createCredentialWithAccessTokenOnly(String token) {
		return new Credential(BearerToken.authorizationHeaderAccessMethod()).setAccessToken(token);
	}
	private static Credential getCredentials(String login) {
		// Load client secrets.
/*		WebServiceProperties properties = API.propertiesManager
				.getWebServicePropertiesByFullName("google_scopes.webservice");
		String client_id = properties.properties
				.getProperty("drive_client_id", null);
		String client_secret = properties.properties
				.getProperty("drive_client_secret", null);*/
		JsonElement el = API.registry.getValue("xusers/"+login+"/google_scopes/google_drive_token");
		if (el==null)
			return null;
		String token = el.getAsString();
/*
		GoogleClientSecrets.Details web = new GoogleClientSecrets.Details();

		web.setClientId(client_id);
		web.setClientSecret(client_secret);

		GoogleClientSecrets clientSecrets = new GoogleClientSecrets();

		clientSecrets.setWeb(web);*/

		return createCredentialWithAccessTokenOnly(token);
	}
	
	private static String getName(String fullPath){
		int i=fullPath.lastIndexOf("/");
		if (i>=0) {
			return fullPath.substring(i + 1);
		} else {
			i = fullPath.lastIndexOf("\\");
			if (i>=0) {
				return fullPath.substring(i + 1);
			} else {
				return fullPath;
			}
		}
	}
	
	private static String getFolderID(String fullPath, Drive service) {
		String name = getName(fullPath);
		try {
			String pageToken = null;
			do {
				FileList result = service.files().list()
						.setQ("name = '" + name + "' and mimeType = 'application/vnd.google-apps.folder'")
						.setFields("nextPageToken, files(id, name)")
						.setPageToken(pageToken)
						.execute();
				for (File file : result.getFiles()) {
					return file.getId();
				}
				pageToken = result.getNextPageToken();
			} while (pageToken != null);
			return "";
		} catch (Throwable throwable){
			throwable.printStackTrace();
			return null;
		}
	}
}
