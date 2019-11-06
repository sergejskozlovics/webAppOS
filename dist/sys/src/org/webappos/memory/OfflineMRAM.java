package org.webappos.memory;

import java.util.HashSet;
import java.util.Set;

import org.webappos.project.IProject;

import lv.lumii.tda.kernel.TDAKernel;

public class OfflineMRAM implements IMRAM {
	
	private Set<IProject> list = new HashSet<IProject>();
	
	public void addProject(IProject p) {
		list.add(p);
	}

	@Override
	public TDAKernel getTDAKernel(String project_id) {
		for (IProject p : list)
			if (project_id.equals(p.getName()))
				return p.getTDAKernel();

		return null;
	}

	@Override
	public String getProjectFolder(String project_id) {
		for (IProject p : list)
			if (project_id.equals(p.getName()))
				return p.getFolderName();
		return null;
	}

	@Override
	public String getProjectFullAppName(String project_id) {
		for (IProject p : list)
			if (project_id.equals(p.getName()))
				return p.getAppName();
		return null;
	}

	@Override
	public boolean renameActiveProject(String project_id, String new_project_id) {
		for (IProject p : list)
			if (project_id.equals(p.getName()))
				return p.setName(new_project_id);
		return false;
	}

	@Override
	public void faultMRAM(String project_id) {
		for (IProject p : list)
			if (project_id.equals(p.getName())) {
				list.remove(p);
				break;
			}
		
	}

}
