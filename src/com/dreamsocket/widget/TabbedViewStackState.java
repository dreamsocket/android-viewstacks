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

import android.os.Parcel;
import android.os.Parcelable;


public class TabbedViewStackState implements Parcelable
{
	ViewTransition defaultTransition;
	String selectedID;
	HashMap<String, ViewStackState> stackStates;
	
	
	public TabbedViewStackState()
	{
		this.defaultTransition = new ViewTransition();
		this.stackStates = new HashMap<String, ViewStackState>();
	}
	
	
    public int describeContents() 
    {
        return 0;
    }

    
    public void writeToParcel(Parcel p_out, int p_flags) 
    {
    	p_out.writeParcelable(this.defaultTransition, p_flags);
    	p_out.writeString(this.selectedID);
    	p_out.writeMap(this.stackStates);
    }

    
    public static final Parcelable.Creator<TabbedViewStackState> CREATOR = new Parcelable.Creator<TabbedViewStackState>() 
    {
        public TabbedViewStackState createFromParcel(Parcel p_in) 
        {
            return new TabbedViewStackState(p_in);
        }

        public TabbedViewStackState[] newArray(int p_size) 
        {
            return new TabbedViewStackState[p_size];
        }
    };
    
    
    @SuppressWarnings("unchecked")
	private TabbedViewStackState(Parcel p_in) 
    {
        this.defaultTransition = p_in.readParcelable(ViewTransition.class.getClassLoader());
        this.selectedID = p_in.readString();
        this.stackStates = p_in.readHashMap(ViewStackState.class.getClassLoader());
    }	
}
