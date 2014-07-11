package org.apache.stratos.messaging.domain.topology;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class Cartridge implements Subscribable, Scalable {
	
	private String alias; // type
	private String cartridgeId;
	private Dependencies dependencies;
	private Subscribable parent;
	private Group homeGroup;
    private static final Log log = LogFactory.getLog(Cartridge.class);

	public Cartridge(String alias) {
		this.alias = alias;
	}
	
	public void setAlias(String alias) {
		this.alias = alias;
	}
	
	public String getAlias() {
		return alias;
	}

	public String getCartridgeId() {
		return cartridgeId;
	}

	public void setCartridgeId(String cartridgeId) {
		this.cartridgeId = cartridgeId;
	}

	public Subscribable getParent() {
		return parent;
	}

	public Group getHomeGroup() {
		return homeGroup;
	}

	public void setHomeGroup(Group homeGroup) {
		this.homeGroup = homeGroup;
	}

	public void setParent(Subscribable parent) {
		this.parent = parent;
	}

	public Dependencies getDependencies() {
		return dependencies;
	}
	
	public List<Subscribable>  getAllDependencies() {
		return new ArrayList<Subscribable>(0);
	}

	public void setDependencies(Dependencies dependencies) {
		this.dependencies = dependencies;
	}

	public void subscribe() {	
	}
	
	public void unsubscribe() {	
	}
	
	public String toString() {
		String result = "";
		result = alias;
		if (dependencies != null) {
			result = result + " dependencies:" + dependencies.toString();
		} 
		return result;
	}

}