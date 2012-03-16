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

import com.dreamsocket.widget.ViewStack.ViewStackEntry;

import android.os.Parcel;
import android.os.Parcelable;


public class ViewStackState implements Parcelable
{
	Boolean addToViewEnabled;
	ViewTransition defaultPopTransition;
	ViewTransition defaultPushTransition;
	ArrayList<ViewStackEntry> items;
	
	
	public ViewStackState()
	{
		this.defaultPopTransition = new ViewTransition();
		this.defaultPushTransition = new ViewTransition();
		this.items = new ArrayList<ViewStackEntry>();
	}
	
	
    public int describeContents() 
    {
        return 0;
    }

    
    public void writeToParcel(Parcel p_out, int p_flags) 
    {
    	p_out.writeInt(this.addToViewEnabled ? 0 : 1);
    	p_out.writeParcelable(this.defaultPopTransition, p_flags);
    	p_out.writeParcelable(this.defaultPushTransition, p_flags);
    	p_out.writeList(this.items);
    }

    
    public static final Parcelable.Creator<ViewStackState> CREATOR = new Parcelable.Creator<ViewStackState>() 
    {
        public ViewStackState createFromParcel(Parcel p_in) 
        {
            return new ViewStackState(p_in);
        }

        public ViewStackState[] newArray(int p_size) 
        {
            return new ViewStackState[p_size];
        }
    };
    
    
    @SuppressWarnings("unchecked")
	private ViewStackState(Parcel p_in) 
    {
    	this.addToViewEnabled = p_in.readInt() == 0;
        this.defaultPopTransition = p_in.readParcelable(ViewTransition.class.getClassLoader());
        this.defaultPushTransition = p_in.readParcelable(ViewTransition.class.getClassLoader());
        this.items = p_in.readArrayList(ViewStackEntry.class.getClassLoader());
    }	
}
