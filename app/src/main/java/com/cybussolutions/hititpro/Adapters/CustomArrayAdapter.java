package com.cybussolutions.hititpro.Adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cybussolutions.hititpro.Activities.MainActivity;
import com.cybussolutions.hititpro.Model.Checkbox_model;
import com.cybussolutions.hititpro.R;

import java.util.List;


public class CustomArrayAdapter extends ArrayAdapter<Checkbox_model> implements
		View.OnClickListener {
	
	private LayoutInflater layoutInflater;
	String[] dbEnterArray;
	private final List<Checkbox_model> list;
	Context context;
	AlertDialog b;

	public CustomArrayAdapter(Context context, List<Checkbox_model> objects) {
		super(context, 0, objects);
		layoutInflater = LayoutInflater.from(context);
		list= objects;
		this.context = context;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		// holder pattern
		Holder holder = null;
		if (convertView == null) {
			holder = new Holder();

			convertView = layoutInflater.inflate(R.layout.listview_row, parent, false);
			holder.setTextViewTitle((TextView) convertView
					.findViewById(R.id.textViewTitle));
			holder.setCheckBox((CheckBox) convertView
					.findViewById(R.id.checkBox));

			holder.delete = (ImageView) convertView.findViewById(R.id.delete);
			holder.edit = (ImageView) convertView.findViewById(R.id.edit);
			holder.imageEditor = (ImageView) convertView.findViewById(R.id.imageEditor);

			convertView.setTag(holder);
		} else {
			holder = (Holder) convertView.getTag();
		}

		SharedPreferences sp=context.getSharedPreferences("prefs", Context.MODE_PRIVATE);

		if(sp.getBoolean("imageButton",true)!=true){
			try {
				ImageView iv= (ImageView) convertView.findViewById(R.id.imageEditor);
				iv.setVisibility(View.INVISIBLE);

			}catch (Exception e){
			}

		}

		holder.delete.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				final AlertDialog.Builder builder = new AlertDialog.Builder(context);
				builder.setMessage("Are you sure?").setPositiveButton("Yes", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialogInterface, int i) {
						list.remove(position);
						notifyDataSetChanged();
					}
				})
						.setNegativeButton("No", new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialogInterface, int i) {


							}
						}).show();

			}
		});


		holder.imageEditor.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				try {
					Intent intent=new Intent(context,MainActivity.class);

					context.startActivity(intent);
				}catch (Exception e){
					Toast.makeText(context,e.toString(),Toast.LENGTH_LONG).show();
				}

			}
		});


		final Holder finalHolder = holder;
		holder.edit.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {

				editItem(position, finalHolder.getTextViewTitle().getText().toString());

			}
		});

		dbEnterArray = new String[list.size() + 1];

		String splitter = "%";
		String rows[] = list.get(position).getTitle().split(splitter);







		for (int i = 0; i < list.size(); i++) {
			dbEnterArray[i] = list.get(i).getTitle();
		}

		final Checkbox_model row = getItem(position);
		holder.getTextViewTitle().setText(rows[0]);
		holder.getCheckBox().setTag(position);
		holder.getCheckBox().setChecked(row.isChecked());
		holder.getCheckBox().setOnClickListener(this);

		changeBackground(getContext(), holder.getCheckBox(),position);

		return convertView;
	}


	@Override
	public void onClick(View v) {

		CheckBox checkBox = (CheckBox) v;
		int position = (Integer) v.getTag();
		getItem(position).setChecked(checkBox.isChecked());

		changeBackground(CustomArrayAdapter.this.getContext(), checkBox , position);

	}
	

	/**
	 * Set the background of a row based on the value of its checkbox value.
	 * Checkbox has its own style.
	 */
	@SuppressWarnings("deprecation")
	private void changeBackground(Context context, CheckBox checkBox,int positon) {
		View row = (View) checkBox.getParent();
		Drawable drawable = context.getResources().getDrawable(
				R.drawable.listview_selector_checked);
		String splitter = "%";
		String row1[] = list.get(positon).getTitle().split(splitter);

		if (checkBox.isChecked()) {
			drawable = context.getResources().getDrawable(
					R.drawable.listview_selector_checked);

			dbEnterArray[positon] = row1[0] + "%1";


		} else {

			dbEnterArray[positon] = row1[0] + "%0";
			drawable = context.getResources().getDrawable(
					R.drawable.listview_selector);
		}
		row.setBackgroundDrawable(drawable);
	}

	static class Holder {
		TextView textViewTitle;
		TextView textViewSubtitle;
		CheckBox checkBox;
		ImageView edit, delete,imageEditor;

		public TextView getTextViewTitle() {
			return textViewTitle;
		}

		public void setTextViewTitle(TextView textViewTitle) {
			this.textViewTitle = textViewTitle;
		}


		public CheckBox getCheckBox() {
			return checkBox;
		}

		public void setCheckBox(CheckBox checkBox) {
			this.checkBox = checkBox;
		}

	}

	void editItem(final int position, final String text) {

		AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
		final View dialogView = layoutInflater.inflate(R.layout.add_dalogbox, null);
		dialogBuilder.setView(dialogView);
		dialogBuilder.setCancelable(false);

		// intializing variables
		final EditText Add = (EditText) dialogView.findViewById(R.id.add_ET);
		final Button to = (Button) dialogView.findViewById(R.id.add_BT);

		Add.setText(text);

		to.setText("Edit Item");

		b = dialogBuilder.create();
		b.show();

		to.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {

				// clearing list view

				list.clear();
				notifyDataSetChanged();

				for (int item = 0; item < dbEnterArray.length; item++) {
					Checkbox_model model = new Checkbox_model();
					model.setTitle(dbEnterArray[item]);

					list.add(model);

					if (item == (dbEnterArray.length - 1)) {
						list.remove(position);

						if (Add.getText().toString().equals("")) {
							Toast.makeText(context, "Please Enter Some Data !!", Toast.LENGTH_SHORT).show();
						} else {
							model.setTitle(Add.getText().toString() + "%0");

							list.add(position, model);

							list.remove(list.size() - 1);
						}

					}

				}

				notifyDataSetChanged();

				b.dismiss();

			}
		});


	}


	public String[] getDbInsertArray() {

		return dbEnterArray;
	}




}
