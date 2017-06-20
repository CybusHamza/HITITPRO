package com.cybussolutions.hititpro.Adapters;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cybussolutions.hititpro.Activities.Detailed_Activity_All_Screens;
import com.cybussolutions.hititpro.Activities.MainActivity;
import com.cybussolutions.hititpro.Activities.StructureScreensActivity;
import com.cybussolutions.hititpro.Model.Checkbox_model;
import com.cybussolutions.hititpro.R;
import com.cybussolutions.hititpro.Sql_LocalDataBase.Database;

import java.sql.SQLException;
import java.util.List;


public class CustomArrayAdapter extends ArrayAdapter<Checkbox_model> implements
		View.OnClickListener {
	
	private LayoutInflater layoutInflater;
	String[] dbEnterArray;
	private final List<Checkbox_model> list;
	Context context;
	AlertDialog b;
	String topass[],enteredStructure = "";
	static  int count=0;
	Database database;

	public CustomArrayAdapter(Context context, List<Checkbox_model> objects,String topass[]) {
		super(context, 0, objects);
		layoutInflater = LayoutInflater.from(context);
		list= objects;
		this.context = context;
		this.topass= topass;
		database= new Database(context);


		dbEnterArray = new String[list.size() + 1];


		for (int i = 0; i < list.size(); i++) {
			dbEnterArray[i] = list.get(i).getTitle();
		}

		/*try {
			database = database.open();
		} catch (SQLException e) {
			e.printStackTrace();
		}*/
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
					public void onClick(DialogInterface dialogInterface, int t) {

						list.clear();
						for (int i = 0; i < dbEnterArray.length -1; i++) {
							Checkbox_model model = new Checkbox_model();
							model.setTitle(dbEnterArray[i]);

							list.add(model);
						}

						list.remove(position);


						dbEnterArray = new String[list.size()];

						for (int i = 0; i < list.size(); i++) {
							dbEnterArray[i] = list.get(i).getTitle();
						}

						Intent intent= new Intent(getContext(), Detailed_Activity_All_Screens.class).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
						intent.putExtra("items",dbEnterArray);
						intent.putExtra("inspectionID", StructureScreensActivity.inspectionID);
						intent.putExtra("heading", topass[0]);
						intent.putExtra("fromAddapter","edit");
						intent.putExtra("column", topass[1]);
						intent.putExtra("dbTable",topass[2]);
						((Activity)context).finish();
						context.startActivity(intent);





					}
				})
						.setNegativeButton("No", new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialogInterface, int i) {


							}
						}).show();

			}
		});
		final Holder finalHolder = holder;

		holder.imageEditor.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				try {

					String name = finalHolder.getTextViewTitle().getText().toString().replaceAll("\\s+","");
					int pagePosition= 0;
					switch (topass[2]){
						case "portfolio":
							pagePosition= 1;
							break;
						case "roofing":
							pagePosition= 2;
							break;
						case "exterior":
							pagePosition= 3;
							break;
						case "interior":
							pagePosition= 9;
							break;
						case "heating":
							pagePosition= 5;
							break;
						case "cooling":
							pagePosition= 6;
							break;
						case "electrical":
							pagePosition= 4;
							break;
						case "insulation":
							pagePosition= 7;
							break;
						case "plumbing":
							pagePosition= 8;
							break;
						case "appliance":
							pagePosition= 10;
							break;
						case "fireplaces":
							pagePosition= 11;
							break;

						default:
							pagePosition= 0;

					}

					String data = pagePosition+"_"+topass[1]+"_"+name;

					Intent intent=new Intent(context,MainActivity.class);
					intent.putExtra("data",data);
					intent.putExtra("dbTable",topass[1]);
					intent.putExtra("showImages","false");
					intent.putExtra("clientId",StructureScreensActivity.client_id);
					intent.putExtra("inspectionId",StructureScreensActivity.inspectionID);
					intent.putExtra("templateId",StructureScreensActivity.template_id);
					context.startActivity(intent);
				}catch (Exception e){
					Toast.makeText(context,e.toString(),Toast.LENGTH_LONG).show();
				}

			}
		});



		holder.edit.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {

				editItem(position, finalHolder.getTextViewTitle().getText().toString());

			}
		});



		String splitter = "%";
		String rows[] = list.get(position).getTitle().split(splitter);

		final Holder finalHolder1 = holder;




		final Checkbox_model row = getItem(position);
		holder.getTextViewTitle().setText(rows[0]);
		holder.getCheckBox().setTag(position);
		holder.getCheckBox().setChecked(row.isChecked());
		holder.getCheckBox().setOnClickListener(this);




		return convertView;
	}


	@Override
	public void onClick(View v) {

		CheckBox checkBox = (CheckBox) v;
		int position = (Integer) v.getTag();
		getItem(position).setChecked(checkBox.isChecked());

		String splitter = "%";
		String row1[] = list.get(position).getTitle().split(splitter);

		if(checkBox.isChecked())
		{

			dbEnterArray[position] = row1[0] + "%1";
		}
		else
		{
			dbEnterArray[position] = row1[0] + "%0";
		}


	}
	

	/**
	 * Set the background of a row based on the value of its checkbox value.
	 * Checkbox has its own style.
	 */

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
		final Button cancel=(Button)dialogView.findViewById(R.id.cancel);

		//Add.setSelection(Add.getText().length())
		Add.requestFocus();


		Add.setText(text);
		Add.setSelection(Add.getText().length());
		InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.toggleSoftInput(InputMethodManager.SHOW_FORCED,0);
		imm.showSoftInput(Add, InputMethodManager.SHOW_IMPLICIT);

		to.setText("Edit Item");

		b = dialogBuilder.create();
		b.show();

		to.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {

				// clearing list view


			/*	for (int i = 0; i < dbEnterArray.length - 1; i++) {
					enteredStructure += dbEnterArray[i] + "^";
				}

				enteredStructure = enteredStructure.substring(0, enteredStructure.length() - 1);

				// Insert in local DataBase*/


				list.clear();

				for (int item = 0; item < dbEnterArray.length; item++) {
					Checkbox_model model = new Checkbox_model();
					model.setTitle(dbEnterArray[item]);

					list.add(model);

					if (item == (dbEnterArray.length - 1)) {
						if (Add.getText().toString().equals("")) {
							Toast.makeText(context, "Please Enter Some Data !!", Toast.LENGTH_SHORT).show();
						} else {

							String row[] =  list.get(position).getTitle().split("%");
							if(row[1].equals("1"))
							{
								model.setTitle(Add.getText().toString() + "%1");
							}
							else
							{
								model.setTitle(Add.getText().toString() + "%0");
							}


							list.remove(position);

							list.add(position, model);

							list.remove(list.size() - 1);

							for (int i = 0; i < list.size(); i++) {
								dbEnterArray[i] = list.get(i).getTitle();
							}

						}

					}

				}

				Intent intent= new Intent(getContext(), Detailed_Activity_All_Screens.class).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
				intent.putExtra("items",dbEnterArray);
				intent.putExtra("inspectionID", StructureScreensActivity.inspectionID);
				intent.putExtra("heading", topass[0]);
				intent.putExtra("fromAddapter","true");
				intent.putExtra("column", topass[1]);
				intent.putExtra("dbTable",topass[2]);
				((Activity)context).finish();
				context.startActivity(intent);

				b.dismiss();
				InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
				imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
				imm.hideSoftInputFromWindow(Add.getWindowToken(), 0);

			}
		});
		cancel.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				b.dismiss();
				InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
				imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
				imm.hideSoftInputFromWindow(Add.getWindowToken(), 0);
			}
		});


	}


	public String[] getDbInsertArray() {

		return dbEnterArray;
	}




}
