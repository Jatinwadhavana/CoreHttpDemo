package com.example.corehttpdemo.employee;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.databinding.DataBindingUtil;

import com.example.corehttpdemo.LoginActivity;
import com.example.corehttpdemo.R;
import com.example.corehttpdemo.databinding.ActivityAddUpdateDataBinding;
import com.example.corehttpdemo.employee.models.EmployeeModel;
import com.example.corehttpdemo.employee.tables.TBL_Employee;
import com.example.corehttpdemo.utils.SharedPref;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.text.DecimalFormat;
import java.util.Calendar;

public class AddUpdateData extends BaseActivity implements DatePickerDialog.OnDateSetListener{


    private boolean forEdit = false;
    private SQLiteDatabase empDatabase=null;
    private ActivityAddUpdateDataBinding activityAddUpdateDataBinding = null;
    private EmployeeModel employeeModel;

    private int dayOfMonth=0;
    private int monthOfYear=0;
    private int year=0;
    private final Calendar now = Calendar.getInstance();
    private final DecimalFormat formatter = new DecimalFormat("00");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityAddUpdateDataBinding = DataBindingUtil.setContentView(this, R.layout.activity_add_update_data);
        forEdit = getIntent().getBooleanExtra("forEdit", false);
        activityAddUpdateDataBinding.tvUserName.setText("Welcome: "+SharedPref.getAppUserDisplayName(this));
        setTitle(getString(R.string.tit_emp_details));
        activityAddUpdateDataBinding.imgLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPref.setAppUserDisplayName(AddUpdateData.this,"");
                SharedPref.setAppIsLogin(AddUpdateData.this,false);
                Intent intentHome=new Intent(AddUpdateData.this, LoginActivity.class);
                intentHome.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intentHome.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intentHome);
                finish();
            }
        });

        if(year == 0){
            year=now.get(Calendar.YEAR);
            monthOfYear=now.get(Calendar.MONTH);
            dayOfMonth=now.get(Calendar.DAY_OF_MONTH);
        }

        if (forEdit) {
            activityAddUpdateDataBinding.btnAdd.setText(R.string.update);
            activityAddUpdateDataBinding.btnDelete.setText(R.string.delete);

            activityAddUpdateDataBinding.edtEmpNo.setEnabled(false);
            employeeModel = (EmployeeModel) getIntent().getSerializableExtra("row");

            activityAddUpdateDataBinding.edtEmpDob.setText(employeeModel.geteDob());
            activityAddUpdateDataBinding.edtEmpName.setText(employeeModel.geteName());
            activityAddUpdateDataBinding.edtEmpNo.setText(employeeModel.geteNo());

        } else {
            activityAddUpdateDataBinding.btnAdd.setText(R.string.add);
            activityAddUpdateDataBinding.btnDelete.setText(R.string.show_list);
        }

        empDatabase = baseDB.getWritableDatabase();

        activityAddUpdateDataBinding.edtEmpDob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog dpd = DatePickerDialog.newInstance(AddUpdateData.this, year, monthOfYear, dayOfMonth);
                dpd.show(getSupportFragmentManager(), "Filter Date");
                dpd.setAccentColor(getResources().getColor(R.color.purple_500));
                dpd.setVersion(DatePickerDialog.Version.VERSION_2);
                dpd.setMaxDate(now);
            }
        });
        activityAddUpdateDataBinding.btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isValid()) {
                    String eNo = activityAddUpdateDataBinding.edtEmpNo.getText().toString();
                    String eName = activityAddUpdateDataBinding.edtEmpName.getText().toString();
                    String eDob = activityAddUpdateDataBinding.edtEmpDob.getText().toString();

                    ContentValues contentValues = new ContentValues();
                    contentValues.put(TBL_Employee.EMP_ID, Integer.parseInt(eNo));
                    contentValues.put(TBL_Employee.EMP_NAME, eName);
                    contentValues.put(TBL_Employee.EMP_DOB, eDob);

                    if (forEdit) {
                        int rowEffected = empDatabase.update(TBL_Employee.TABLE_NAME, contentValues, TBL_Employee.EMP_ID + " = ?", new String[]{eNo});
                        Toast.makeText(AddUpdateData.this, "Row Effected=" + rowEffected, Toast.LENGTH_LONG).show();
                    } else {
                        if (empDatabase.insert(TBL_Employee.TABLE_NAME, null, contentValues) > 0) {
                            Toast.makeText(AddUpdateData.this, "Saved", Toast.LENGTH_LONG).show();

                            Intent intentListing = new Intent(AddUpdateData.this, EmployeeList.class);
                            startActivity(intentListing);

                        } else {
                            Toast.makeText(AddUpdateData.this, "Error", Toast.LENGTH_LONG).show();
                        }
                    }
                } else {
                    Toast.makeText(AddUpdateData.this, "Input error", Toast.LENGTH_LONG).show();
                }
            }
        });
        activityAddUpdateDataBinding.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (forEdit) {
                        //Delete
                        empDatabase.execSQL(String.format("DELETE FROM " + TBL_Employee.TABLE_NAME
                                + " WHERE " + TBL_Employee.EMP_ID
                                + " IN (%s);", employeeModel.geteNo()));
                        Toast.makeText(AddUpdateData.this, "Record removed!!", Toast.LENGTH_LONG).show();
                        finish();
                    } else {
                        Intent intentListing = new Intent(AddUpdateData.this, EmployeeList.class);
                        startActivity(intentListing);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private boolean isValid() {
        String eNo = activityAddUpdateDataBinding.edtEmpNo.getText().toString();
        String eName = activityAddUpdateDataBinding.edtEmpName.getText().toString();
        String eDob = activityAddUpdateDataBinding.edtEmpDob.getText().toString();

        return (eNo.trim().length() > 0 && eName.trim().length() > 0 && eDob.trim().length() > 0);
    }
    private String getFormattedDate()  {
        return year+"-"+ formatter.format((monthOfYear + 1)) +"-"+ formatter.format(dayOfMonth);
    }
    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        this.dayOfMonth = dayOfMonth;
        this.monthOfYear = monthOfYear;
        this.year=year;
        activityAddUpdateDataBinding.edtEmpDob.setText(getFormattedDate());
    }
}
