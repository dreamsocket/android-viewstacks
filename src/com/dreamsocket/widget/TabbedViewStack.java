/**
 * Dreamsocket
 * 
 * Copyright 2011 Dreamsocket.
 * All Rights Reserved. 
 *
 * This software (the "Software") is the property of Dreamsocket and is protected by U.S. and
 * international intellectual property laws. No license is granted with respect to the
 * software and users may not, among other things, reproduce, prepare derivative works
 * of, modify, distribute, sublicense, reverse engineer, disassemble, remove, decompile,
 * or make any modifications of the Software without written permission from Dreamsocket.
 * Further, Dreamsocket does not authorize any user to remove or alter any trademark, logo,
 * copyright or other proprietary notice, legend, symbol, or label in the Software.
 * This notice is not intended to, and shall not, limit any rights Dreamsocket has under
 * applicable law.
 * 
 */
 
package com.dreamsocket.widget;

import java.util.HashMap;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;


public class TabbedViewStack
{
	protected int m_containerViewID;
	protected ViewTransition m_defaultTransition;
	protected FragmentManager m_fragMgr;
	protected HashMap<String, ViewStack> m_navigators;
	protected String m_selectedID;
	
	
	public TabbedViewStack(int p_containerViewID, FragmentManager p_fragMgr)
	{
		this.m_containerViewID = p_containerViewID;
		this.m_fragMgr = p_fragMgr;
		
		this.m_navigators = new HashMap<String, ViewStack>();
	}
	
	
	public TabbedViewStack(int p_containerViewID, FragmentManager p_fragMgr, TabbedViewStackState p_state)
	{
		this.m_containerViewID = p_containerViewID;
		this.m_fragMgr = p_fragMgr;
		
		this.m_navigators = new HashMap<String, ViewStack>();

		this.m_defaultTransition = p_state.defaultTransition;
		this.m_selectedID = p_state.selectedID;		
		for(String key : p_state.stackStates.keySet()) 
		{
			this.m_navigators.put(key, new ViewStack(this.m_containerViewID, this.m_fragMgr, p_state.stackStates.get(key)));
		}	
	}	
	
	

	public ViewStack getSelected()
	{
		return this.m_navigators.get(this.m_selectedID);
	}
	
	
	public String getSelectedID()
	{
		return this.m_selectedID;
	}
	
	
	public TabbedViewStackState getState()
	{
		TabbedViewStackState data = new TabbedViewStackState();
		
		data.defaultTransition = this.m_defaultTransition;
		data.selectedID = this.m_selectedID;
		
		HashMap<String, ViewStack> navigators = this.m_navigators;
		
		for(String key : navigators.keySet()) 
		{
			data.stackStates.put(key, navigators.get(key).state());
		}		
		
		return data;
	}
	
	
	public void selectID(String p_ID)
	{
		this.selectID(p_ID, null);
	}	
	
	
	public void selectID(String p_ID, ViewTransition p_transition)
	{
		if(p_ID != this.m_selectedID)
		{
			ViewTransition defaultTrans = this.m_defaultTransition == null ? TabbedViewStackDefaults.defaultTransition : this.m_defaultTransition;
        	ViewTransition transition = p_transition == null ? defaultTrans : p_transition;
        	FragmentTransaction ft = this.m_fragMgr.beginTransaction();
     
            
            if(transition.in != -1)
            {
            	ft.setCustomAnimations(transition.in, transition.out);
            }
            
        	if(this.getSelected() != null)
        	{	
        		this.getSelected().m_addToViewEnabled = false;
        		if(this.getSelected().selected() != null)
        			ft.detach(this.getSelected().selected());
        	}    
        	
            this.m_selectedID = p_ID;
            
            if(this.getSelected() != null)
        	{	
        		this.getSelected().m_addToViewEnabled = true;
        		
        		if(this.getSelected().selected() != null)
        			ft.attach(this.getSelected().selected());
        	}  
            
            ft.commit();			
		}
	}
	
	public ViewTransition defaultTransition()
	{
		return this.m_defaultTransition;
	}	
	
	
	public void defaultTransition(ViewTransition p_value)
	{
		this.m_defaultTransition = p_value;
	}
	
	
	public ViewStack add(String p_ID)
	{
		if(!this.m_navigators.containsKey(p_ID))
		{
			this.m_navigators.put(p_ID, new ViewStack(this.m_containerViewID, this.m_fragMgr));
			this.m_navigators.get(p_ID).m_addToViewEnabled = false;
		}
		
		return this.m_navigators.get(p_ID);
	}
	
	
	public ViewStack get(String p_ID)
	{
		return this.m_navigators.get(p_ID);
	}
	
	
	public void clear()
	{
		HashMap<String, ViewStack> navigators = this.m_navigators;
		
		for(String key : navigators.keySet()) 
		{
			navigators.get(key).remove(-1);
		}		
				
		this.m_navigators.clear();
	}
	
	
	public Boolean popSelectedView()
	{
    	if(this.getSelected() != null && this.getSelected().length() > 1)
    	{
    		this.getSelected().remove();		
    		return true;
    	}
    	
    	return false;
	}
	
	
	public void remove(String p_ID)
	{
		if(p_ID == this.m_selectedID)
		{	// remove from view
			// just remove from manager
		}
		this.m_navigators.remove(p_ID);
	}	
}
