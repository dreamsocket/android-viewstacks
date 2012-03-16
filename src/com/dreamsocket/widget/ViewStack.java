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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.UUID;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;



/*
 * to(1) move to index 1
 * to(-3) move the item that is 3rd item from the end
 * 
 * remove(int[]) removes all of the indices defined
 * remove(1) - pop to front
 * remove(0) - remove all
 * remove(1, 3) - remove indexes 1-3 
 * remove(-5, -1) - remove the last 4 items
 * 
 * add(stack.index()+1, frag).next() - add an item, push to it
 * add(1, frag) add at index of 1
 * add(1, frag).to(1) add at index of 1 and move to it
 * add(-2, frag) add two back from the end
 * add(1, frag, "id") add frag at index of 1 tag it with id
 * add(frag) add to the end
 * add(frag, "id") add frag at end tag it with id
 * 
 * replace(1, frag) replace at index of 1
 * replace(-2, frag) replace frag two back from the end
 * replace(frag) replace current frag
 * replace(frag, "id") replace current frag tag it with id
 * replace(1, frag, "id") replace frag at index of 1 tag it with id
 * 
 * next() - short cut for to(index() + 1)
 * next(viewTransition)
 * prev() - short cut for to(index() - 1)
 * prev(viewTransition)
 * 
 * get(0) - get item at index 0
 * 
 * lastIndexOf("id") - returns index of the last found item tagged id
 * lastIndexOf("id", 3) - return array of all indices of items tagged with id
 * 
 * firstIndexOf("id") - returns index of the last found item tagged id
 * firstIndexOf("id", 3) - return array of all indices of items tagged with id
 * 
 * indicesOf("id") - returns index of the first found item tagged id
 * indicesOf("id", 0) - returns array of all indices of items tagged with id after the index of 3
 * indicesOf("id", 0, 3) - returns array of all indices of items tagged with id after the index of 3
 * 
 * count("id") - return the count of items tagged with id
 * 
 * has("id") - return whether any items are tagged with id
 */
public class ViewStack
{
	protected Boolean m_addToViewEnabled;
	protected int m_containerViewID;
	protected ViewTransition m_defaultPopTransition;
	protected ViewTransition m_defaultPushTransition;
	protected ArrayList<ViewStackEntry> m_fragEntries;
	protected FragmentManager m_fragMgr;
	protected int m_index;
	
	
	public ViewStack(int p_containerViewID, FragmentManager p_fragMgr)
	{
		this.m_addToViewEnabled = true;
		this.m_containerViewID = p_containerViewID;
		this.m_fragMgr = p_fragMgr;
		this.m_fragEntries = new ArrayList<ViewStackEntry>();
	}

	
	public ViewStack(int p_containerViewID, FragmentManager p_fragMgr, ViewStackState p_state)
	{
		this.m_addToViewEnabled = p_state.addToViewEnabled;
		this.m_containerViewID = p_containerViewID;
		this.m_defaultPopTransition = p_state.defaultPopTransition;
		this.m_defaultPushTransition = p_state.defaultPushTransition;
		this.m_fragMgr = p_fragMgr;
		this.m_fragEntries = p_state.items;
	}
	
	

	public ViewTransition defaultPopTransition()
	{
		return this.m_defaultPopTransition;
	}	
	
	
	public ViewStack defaultPopTransition(ViewTransition p_value)
	{
		this.m_defaultPopTransition = p_value;
		
		return this;
	}
	
	
	public ViewTransition defaultPushTransition()
	{
		return this.m_defaultPushTransition;
	}
	
	
	public ViewStack defaultPushTransition(ViewTransition p_value)
	{
		this.m_defaultPushTransition = p_value;
		
		return this;
	}
	
	
	public int index()
	{
		return this.m_index;
	}
	

	public int length()
	{
		return this.m_fragEntries.size();
	}


	public Fragment selected()
	{
		return this.m_fragEntries.size() > 0 ? this.m_fragMgr.findFragmentByTag(this.m_fragEntries.get(this.m_index).fragmentTag) : null;
	}	
	
	
	public ViewStackState state()
	{
		ViewStackState data = new ViewStackState();
		
		data.addToViewEnabled = this.m_addToViewEnabled;
		data.defaultPopTransition = this.m_defaultPopTransition;
		data.defaultPushTransition = this.m_defaultPushTransition;
		data.items = this.m_fragEntries;
		
		return data;
	}		
	
	
	public ViewStack add(Fragment p_fragment)
	{
		return this.doAddTransaction(this.length(), p_fragment, null);
	}

	
	public ViewStack add(int p_index, Fragment p_fragment)
	{
		return this.doAddTransaction(p_index, p_fragment, null);
	}
	
	
	public ViewStack add(Fragment p_fragment, String p_tag)
	{
		return this.doAddTransaction(this.length(), p_fragment, p_tag);
	}
	
	
	public ViewStack add(int p_index, Fragment p_fragment, String p_tag)
	{
		return this.doAddTransaction(p_index, p_fragment, p_tag);
	}
	
	
	public int count(String p_tag)
	{
		return this.indicesOf(p_tag, 0, -1).length;
	}
	
	
	public Boolean has(String p_tag)
	{
		return this.lastIndexOf(p_tag) != -1;
	}
	
	
	public int lastIndexOf(String p_tag)
	{
		return this.lastIndexOf(p_tag, -1);
	}
	
	
	public int lastIndexOf(String p_tag, int p_fromIndex)
	{
		if(this.m_fragEntries.size() == 0) return -1;
		
		int i = this.getIndexValue(p_fromIndex, 1);
		while(i > -1)
		{
			if(this.m_fragEntries.get(i).identifierTag == p_tag)
				return i;

			i--;
		}
		
		return -1;
	}
	

	public int firstIndexOf(String p_tag)
	{
		return this.firstIndexOf(p_tag, 0);
	}
	
	
	public int firstIndexOf(String p_tag, int p_startIndex)
	{
		if(this.m_fragEntries.size() == 0) return -1;
		
		int i = this.getIndexValue(p_startIndex, -1);
		int len = this.m_fragEntries.size();
		while(i < len)
		{
			if(this.m_fragEntries.get(i).identifierTag == p_tag)
				return i;
			i++;
		}
		
		return -1;
	}	
	
	
	public Integer[] indicesOf(String p_tag)
	{
		return this.indicesOf(p_tag, 0, -1);
	}
	
	
	public Integer[] indicesOf(String p_tag, int p_startIndex)
	{
		return this.indicesOf(p_tag, p_startIndex, -1);
	}	
	
	
	public Integer[] indicesOf(String p_tag, int p_startIndex, int p_endIndex)
	{
		ArrayList<Integer> indices = new ArrayList<Integer>();
		
		if(this.m_fragEntries.size() > 0)
		{
			int startIndex = this.getIndexValue(p_startIndex, -1);
			int endIndex = Math.max(startIndex, this.getIndexValue(p_endIndex, -1)) + 1;
			while(startIndex < endIndex)
			{
				if(this.m_fragEntries.get(startIndex).identifierTag == p_tag)
					indices.add(startIndex);
				startIndex++;
			}
		}

		return (Integer[]) indices.toArray(new Integer[indices.size()]);
	}	
	
	
	public Fragment get(int p_index)
	{
		return p_index < this.m_fragEntries.size() ? this.m_fragMgr.findFragmentByTag(this.m_fragEntries.get(p_index).fragmentTag) : null;
	}
	
	
	
	public ViewStack next()
	{
		return this.next(null);
	}
	
	
	public ViewStack next(ViewTransition p_transition)
	{
		if(this.m_index < this.length() - 1)
		{
			this.to(this.m_index + 1, p_transition);
		}
		return this;
	}
	
	
	public ViewStack prev()
	{
		return this.prev(null);
	}	
	
	
	public ViewStack prev(ViewTransition p_transition)
	{
		if(this.m_index > 0)
		{
			this.to(this.m_index - 1, p_transition);
		}
		return this;
	}
	
	
	public ViewStack remove()
	{
		return this.remove(-1, -1, null);
	}
	
	
	public ViewStack remove(ViewTransition p_transition)
	{
		return this.remove(-1, -1, p_transition);
	}	
	
	
	public ViewStack remove(int p_startIndex)
	{
		return this.remove(p_startIndex, -1, null);
	}	
	
	
	public ViewStack remove(int p_startIndex, ViewTransition p_transition)
	{
		return this.remove(p_startIndex, -1, p_transition);
	}
	
	
	public ViewStack remove(int p_startIndex, int p_endIndex)
	{
		return this.remove(p_startIndex, p_endIndex, null);
	}
	
	
	public ViewStack remove(int p_startIndex, int p_endIndex, ViewTransition p_transition)
	{
        if(this.selected() != null && p_startIndex < this.length()) 
        {
    		int startIndex = this.getIndexValue(p_startIndex, -1);
    		int endIndex = Math.max(startIndex, this.getIndexValue(p_endIndex, -1));
    		Integer[] indices = new Integer[endIndex + 1 - startIndex];
    		int i = indices.length;
    		while(i > 0)
    		{
    			indices[--i] = endIndex--;
    		}
    		return remove(indices, p_transition);
        }
        
        return this;
	}
	
	
	public ViewStack remove(Integer[] p_indices)
	{
		return this.remove(p_indices, null);
	}
	
	
	public ViewStack remove(Integer[] p_indices, ViewTransition p_transition)
	{
        if(this.selected() != null) 
        {
        	// convert indices to correct values
        	int[] indices = new int[p_indices.length];
        	int i = indices.length;
        	while(i > 0)
        	{	
        		indices[--i] = this.getIndexValue(p_indices[i], -1);
        	}
        	
        	// sort indices numerically
    		Arrays.sort(indices);

    		// determine if changing out current index and what it is
        	Boolean replacingSelected = false;
        	i = indices.length;
        	int currIndex = this.m_index;
    		while(i > 0)
    		{
	    		if(indices[--i] == this.m_index && this.m_addToViewEnabled && !replacingSelected)
	    		{
	    			this.m_index--;
	    			replacingSelected = true;
	    		}
	    		else if(indices[i] < currIndex)
	    		{
	    			this.m_index--;
	    		}
    		}
    		
    		i = indices.length;
        	FragmentTransaction ft = this.m_fragMgr.beginTransaction();
        	if(replacingSelected)
        	{
        		this.m_index = Math.max(0, this.m_index);
        		
        		if(i < this.length())
        		{	// select new index
        			this.setTransition(p_transition, this.m_defaultPopTransition == null ? ViewStackDefaults.defaultPopTransition : this.m_defaultPopTransition, ft); 	
                	ft.attach(this.selected());
        		}
        	}  
        	
        	// remove indices
            while(i > 0)
            {
            	this.doRemove(ft, indices[--i]);
            }
            ft.commit();
        }
        
        return this;
	}
	
	
	public ViewStack replace(Fragment p_fragment)
	{
		return this.replace(this.index(), p_fragment, null, null);
	}	
	
	
	public ViewStack replace(Fragment p_fragment, String p_tag)
	{
		return this.replace(this.index(), p_fragment, p_tag, null);
	}	
	
	
	public ViewStack replace(Fragment p_fragment, ViewTransition p_transition)
	{
		return this.replace(this.index(), p_fragment, null, p_transition);
	}
	
	
	public ViewStack replace(int p_index, Fragment p_fragment)
	{
		return this.replace(p_index, p_fragment, null, null);
	}
	
	
	public ViewStack replace(int p_index, Fragment p_fragment, ViewTransition p_transition)
	{
		return this.replace(p_index, p_fragment, null, p_transition);
	}
	
	
	public ViewStack replace(int p_index, Fragment p_fragment, String p_tag)
	{
		return this.replace(p_index, p_fragment, p_tag, null);
	}
	
	
	public ViewStack replace(int p_index, Fragment p_fragment, String p_tag, ViewTransition p_transition)
	{
        String fragmentTag = UUID.randomUUID().toString(); 
		int index = this.getIndexValue(p_index, -1);
		Boolean indexChanged = index == this.m_index && this.selected() != null && !this.selected().isDetached();
    	FragmentTransaction ft = this.m_fragMgr.beginTransaction();
    	
    	if(indexChanged)
    	{
        	this.setTransition(p_transition, this.m_defaultPushTransition == null ? ViewStackDefaults.defaultPushTransition : this.m_defaultPushTransition, ft);
    	}

    	this.doRemove(ft, index);
        ft.add(this.m_containerViewID, p_fragment, fragmentTag);

        if(!indexChanged)
        {
        	ft.detach(p_fragment);
        }
        
        this.m_fragEntries.add(index, new ViewStackEntry(fragmentTag, p_tag)); 
        
        ft.commit();
        
        return this;
	}	
	
	
	public String tag(int p_value)
	{
		int index = this.getIndexValue(p_value, -1);
		return index < this.m_fragEntries.size() ? this.m_fragEntries.get(index).identifierTag : null;
	}
	
	
	public void tag(int p_index, String p_tag)
	{
		int index = this.getIndexValue(p_index, -1);
		
		if(index < this.m_fragEntries.size())
		{
			this.m_fragEntries.get(index).identifierTag = p_tag;
		}
	}
	
	
	public ViewStack to(int p_index)
	{
		return this.to(p_index, null);
	}
	
	
	public ViewStack to(int p_index, ViewTransition p_transition)
	{
		int pos = this.getIndexValue(p_index, -1);
		
		if(pos != this.m_index || (this.selected() == null || this.selected().isDetached()))
		{
			this.m_fragMgr.executePendingTransactions();
        	
        	FragmentTransaction ft = this.m_fragMgr.beginTransaction();
   
            if(this.get(pos) != null && this.m_addToViewEnabled)
            {
            	ViewTransition popTrans = this.m_defaultPopTransition == null ? ViewStackDefaults.defaultPopTransition : this.m_defaultPopTransition;
            	ViewTransition pushTrans = this.m_defaultPushTransition == null ? ViewStackDefaults.defaultPushTransition : this.m_defaultPushTransition;
            	this.setTransition(p_transition, pos < this.m_index ? popTrans : pushTrans, ft);   
                if(pos != this.m_index)
                {
                	ft.detach(this.get(this.m_index));
                }
            	ft.attach(this.get(pos));
            }
            
            this.m_index = pos;
            
            ft.commit();
			this.m_fragMgr.executePendingTransactions();
		}
		
		return this;
	}
	
	
	protected ViewStack doAddTransaction(int p_index, Fragment p_fragment, String p_tag)
	{
		FragmentTransaction ft = this.m_fragMgr.beginTransaction();
		int newIndex = this.getIndexValue(p_index, 0);   
        String tag = UUID.randomUUID().toString();

        this.m_fragEntries.add(newIndex, new ViewStackEntry(tag, p_tag));
        
        if(newIndex < this.m_index && this.length() > 0)
        {
        	this.m_index++;
        }
        
        ft.add(this.m_containerViewID, p_fragment, tag);
        ft.detach(p_fragment);
	    ft.commit();
	    
	    return this;
	}
	
	
	protected void doRemove(FragmentTransaction p_transaction, int p_index)
	{
		if(this.m_fragEntries.size() == 0) return;
		
		String tag = this.m_fragEntries.remove(p_index).fragmentTag;
		Fragment f = this.m_fragMgr.findFragmentByTag(tag);
		
    	try
    	{
    		p_transaction.remove(f);
    	}
    	catch(Exception e)
    	{
    		p_transaction.add(f, tag);
    		p_transaction.remove(f);
    	}		
	}
	
	
	protected int getIndexValue(int p_value, int p_fromEnd)
	{
		int val = p_value < 0 ? this.length() + p_value : p_value;
		return Math.min(Math.max(0, val), this.length() + p_fromEnd);		
	}
	
	
	protected void setTransition(ViewTransition p_transition, ViewTransition p_defaultTransition, FragmentTransaction p_transaction)
	{
        ViewTransition transition = p_transition == null ? p_defaultTransition : p_transition;
    		
        if(transition.in != -1 && this.m_addToViewEnabled)
        {
        	p_transaction.setCustomAnimations(transition.in, transition.out);
        } 		
	}
	
	
	
	/*
	 * ViewStackEntry - class used to record the fragment added
	 */
	protected static class ViewStackEntry implements Parcelable
	{
		String fragmentTag;
		String identifierTag;
		
		
		public ViewStackEntry(String p_fragmentTag, String p_identifierTag)
		{
			this.fragmentTag = p_fragmentTag;
			this.identifierTag = p_identifierTag == null ? p_fragmentTag : p_identifierTag;
		}
		
		
	    public int describeContents() 
	    {
	        return 0;
	    }

	    
	    public void writeToParcel(Parcel p_out, int p_flags) 
	    {
	    	p_out.writeString(this.fragmentTag);
	    	p_out.writeString(this.identifierTag);
	    }

	    
	    public static final Parcelable.Creator<ViewStackEntry> CREATOR = new Parcelable.Creator<ViewStackEntry>() 
	    {
	        public ViewStackEntry createFromParcel(Parcel p_in) 
	        {
	            return new ViewStackEntry(p_in);
	        }

	        public ViewStackEntry[] newArray(int p_size) 
	        {
	            return new ViewStackEntry[p_size];
	        }
	    };
	    
	    
	    private ViewStackEntry(Parcel p_in) 
	    {
	    	this.fragmentTag = p_in.readString();
	    	this.identifierTag = p_in.readString();
	    }	
	}
	
}
