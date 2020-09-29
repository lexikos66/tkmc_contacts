package com.templarknightsmc.tkmccontacts;

import java.util.List;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class ContactAdapter extends ArrayAdapter<Contact> {
	private final List<Contact> _contacts;
	private final Activity _context;
	
	public ContactAdapter(Activity context, List<Contact> contacts)
	{
		super(context, R.layout.activity_contact_list, contacts);
		this._contacts=contacts;
		this._context=context;
	}
	
	static class ViewHolder {
		protected TextView displayName;
		protected TextView rank;
		protected TextView phone;
		protected TextView email;
		private Contact  _contact;
		protected void setContact(Contact contact)
		{
			displayName.setText(contact.getDisplayName() + " [" + contact.getNickname() + "]");
			rank.setText(contact.getRank());
			phone.setText(contact.getPhone());
			email.setText(contact.getEmail());
			_contact=contact;
		}
		protected Contact getContact() {return _contact;}
	}
	
	@Override
	public Contact getItem(int position)
	{
		return _contacts.get(position);
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		if(convertView==null)
		{
			LayoutInflater inflater = (LayoutInflater) _context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView=inflater.inflate(R.layout.activity_contact_list, null);
		}
		final ViewHolder viewHolder=new ViewHolder();
		viewHolder.displayName=(TextView)convertView.findViewById(R.id.txtDisplayName);
		viewHolder.rank=(TextView)convertView.findViewById(R.id.txtRank);
		viewHolder.phone=(TextView)convertView.findViewById(R.id.txtPhone);
		viewHolder.email=(TextView)convertView.findViewById(R.id.txtEmail);
		viewHolder.setContact(_contacts.get(position));
		convertView.setTag(viewHolder);
		
		//return view;
		return convertView;
	}
}
