package com.example.curatorsttit.ui;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.text.InputType;
import android.transition.AutoTransition;
import android.transition.TransitionManager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.Group;
import androidx.core.view.MenuProvider;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.curatorsttit.R;
import com.example.curatorsttit.adapters.SicknessRecViewAdapter;
import com.example.curatorsttit.common.DataGenerator;
import com.example.curatorsttit.common.DateConverter;
import com.example.curatorsttit.databinding.ActivityMainBinding;
import com.example.curatorsttit.databinding.FragmentStudentInfoBinding;
import com.example.curatorsttit.databinding.FragmentStudentInfoTwoBinding;
import com.example.curatorsttit.models.Addresses;
import com.example.curatorsttit.models.Misses;
import com.example.curatorsttit.models.Passport;
import com.example.curatorsttit.models.Person;
import com.example.curatorsttit.models.StudentData;
import com.example.curatorsttit.network.ApiService;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StudentInfoActivity extends AppCompatActivity{
    private int personID;
    private Toolbar toolbar;
    private Person person;
    private StudentData studentData;
    FragmentStudentInfoBinding binding;
    FragmentStudentInfoTwoBinding binding2;
    SicknessRecViewAdapter sicknessRecViewAdapter;
    RecyclerView sicknessRecView;
    @Override
    public void onCreate(Bundle savedInstanceState) {

        Bundle args = getIntent().getExtras();
        if(args !=  null){

        }

        personID = args.getInt("personID",-1);
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();
        person = gson.fromJson( args.getString("person"),Person.class);

        //ActionBar actionBar = getSupportActionBar();
        //actionBar.setHomeButtonEnabled(true);

        //actionBar.setDisplayHomeAsUpEnabled(true);
        //actionBar.setDisplayShowTitleEnabled(false);

        super.onCreate(savedInstanceState);
        // Inflate the layout for this fragment
        binding2 = FragmentStudentInfoTwoBinding.inflate(LayoutInflater.from(this));
        setContentView(binding2.getRoot());
        initToolbar();
        //FixME нормальную инициализацию скана
        /*------------------------------------*/
        sicknessRecView = findViewById(R.id.recyclerMisses);
        initRecView();
        /*------------------------------------*/

        /*if(getArguments() !=  null)
            personID = getArguments().getInt("personID", -1);
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();
        person = gson.fromJson( getArguments().getString("person"),Person.class);*/
        //setHasOptionsMenu(true);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.action_bar_edit, menu);

        menu.findItem(R.id.edit).setOnMenuItemClickListener(menuItem -> {
            menu.findItem(R.id.edit).setVisible(false);
            menu.findItem(R.id.cancel).setVisible(true);
            menu.findItem(R.id.done).setVisible(true);
            canEdit(true);
            return onOptionsItemSelected(menuItem);
        });

        menu.findItem(R.id.cancel).setOnMenuItemClickListener(menuItem -> {
            menu.findItem(R.id.edit).setVisible(true);
            menu.findItem(R.id.cancel).setVisible(false);
            menu.findItem(R.id.done).setVisible(false);
            canEdit(false);
            clearEdit();
            return onOptionsItemSelected(menuItem);
        });

        menu.findItem(R.id.done).setOnMenuItemClickListener(menuItem -> {
            menu.findItem(R.id.edit).setVisible(true);
            menu.findItem(R.id.cancel).setVisible(false);
            menu.findItem(R.id.done).setVisible(false);
            canEdit(false);
            return onOptionsItemSelected(menuItem);
        });

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onStart() {
        super.onStart();
        mockLoadStudentInfo(person);
        mockLoadMisses();
    }

    void initToolbar(){
        toolbar = (Toolbar) findViewById(R.id.toolbar);//.findViewById(R.id.toolbar);
        //toolbar.setNavigationIcon(R.drawable.ic_arrow_back);
        toolbar.setNavigationIcon(androidx.appcompat.R.drawable.abc_ic_ab_back_material);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (view.getId()) {
                    case android.R.id.home:
                        onBackPressed();
                        break;
                    case -1:
                        onBackPressed();
                        break;
                    case android.R.id.accessibilitySystemActionBack:
                        onBackPressed();
                        break;

                    default: break;
                }
            }
        });
        toolbar.addMenuProvider(new MenuProvider() {
            @Override
            public void onCreateMenu(@NonNull Menu menu, @NonNull MenuInflater menuInflater) {
                menu.clear();
                menuInflater.inflate(R.menu.action_bar_edit, menu);

                menu.findItem(R.id.edit).setOnMenuItemClickListener(menuItem -> {
                    menu.findItem(R.id.done).setVisible(true);
                    menu.findItem(R.id.cancel).setVisible(true);
                    menu.findItem(R.id.edit).setVisible(false);
                    canEdit(true);
                    return onOptionsItemSelected(menuItem);
                });

                menu.findItem(R.id.cancel).setOnMenuItemClickListener(menuItem -> {
                    menu.findItem(R.id.edit).setVisible(true);
                    menu.findItem(R.id.cancel).setVisible(false);
                    menu.findItem(R.id.done).setVisible(false);
                    canEdit(false);
                    clearEdit();
                    return onOptionsItemSelected(menuItem);
                });

                menu.findItem(R.id.done).setOnMenuItemClickListener(menuItem -> {
                    menu.findItem(R.id.edit).setVisible(true);
                    menu.findItem(R.id.cancel).setVisible(false);
                    menu.findItem(R.id.done).setVisible(false);
                    updateStudentData(studentData);
                    canEdit(false);
                    return onOptionsItemSelected(menuItem);
                });
            }

            @Override
            public boolean onMenuItemSelected(@NonNull MenuItem menuItem) {
                return false;
            }
        });
    }
    void clearEdit(){
        //getView().clearFocus();
        completeFields(DataGenerator.mockGetStudent(person));
    }
    void completeFields(StudentData studentData){
        binding2.commonLay.studentSNP.setText(studentData.getPerson().getSNP());
        binding2.commonLay.studentBirth.setText(
                DateConverter.getNyFormattedDate(studentData.getBirthday().toString())
        );
        binding2.commonLay.studentYears.setText(String.valueOf(getAge(studentData.getBirthday())));
        binding2.commonLay.studentEmail.setText(studentData.getPerson().getEmail());
        binding2.commonLay.studentPhone.setText(studentData.getPerson().getPhone());
        //Адресс проживания
        binding2.commonLay.ResAddress.setText(studentData.getResidentialAddress().getAddressAsString());
        binding2.commonLay.dormitoryCheckBox.setChecked(studentData.isDormitory());

        //инн
        binding2.medLay.studentITN.setText(studentData.getiTN());
        //снилс
        binding2.medLay.studentINOILA.setText(studentData.getInsurPolicy());
        //полис
        binding2.medLay.studentIMPNSSeria.setText(studentData.getMedPolicy().substring(0,3));
        binding2.medLay.studentIMPNSNumber.setText(studentData.getMedPolicy().substring(2));
        //опекуны
        Person careTaker = null;
        if(!studentData.getPerens().isEmpty())
            careTaker = studentData.getPerens().get(0);
        if(careTaker != null){
            binding2.caretakertLay.FatherSNP.setText(careTaker.getSNP());
            binding2.caretakertLay.FatherNumber.setText(careTaker.getPhone());
        }
        //паспортные данные
        binding2.passLay.studentPassportSeria.setText(studentData.getPassport().getSeries());
        binding2.passLay.studentPassportNumber.setText(studentData.getPassport().getNumber());
        binding2.passLay.subdivisionCode.setText(studentData.getPassport().getSubdivisionCode());
        binding2.passLay.IssuingAuthority.setText(studentData.getPassport().getIssuingAuthority());
        binding2.passLay.IssueDate.setText(DateConverter.getNyFormattedDate(studentData.getPassport().getIssueDate().toString()));
        //Адресс регистрации
        binding2.passLay.Address.setText(studentData.getRegistrationAddress().getAddressAsString());



        binding2.sickLay.addScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                photoPickerIntent.setType("image/*");
                someActivityResultLauncher.launch(photoPickerIntent);
            }
        });

        binding2.sickLay.addMiss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addMissToStudent();
            }
        });
    }


    // You can do the assignment inside onAttach or onCreate, i.e, before the activity is displayed
    ActivityResultLauncher<Intent> someActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                @SuppressLint("NotifyDataSetChanged")
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        // There are no request codes
                        Intent data = result.getData();
                        Uri selectedImage = data.getData();
                        try (InputStream imageStream = getContentResolver().openInputStream(selectedImage)) {
                            Bitmap yourSelectedImage = BitmapFactory.decodeStream(imageStream);
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        //startActivity(new Intent(Intent.ACTION_VIEW, selectedImage));//Uri.parse("content://media/external/images/media/16")
                        binding2.sickLay.addScan.setText(getFileName(selectedImage));

                    }
                    else{
                        binding2.sickLay.addScan.setText("Выбрать скан");
                    }
                }

            });

    @SuppressLint("Range")
    public String getFileName(Uri uri) {
        String result = null;
        if (uri.getScheme().equals("content")) {
            Cursor cursor = getContentResolver().query(uri, null, null, null, null);
            try {
                if (cursor != null && cursor.moveToFirst()) {
                    result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                }
            } finally {
                cursor.close();
            }
        }
        if (result == null) {
            result = uri.getPath();
            int cut = result.lastIndexOf('/');
            if (cut != -1) {
                result = result.substring(cut + 1);
            }
        }
        return result;
    }

    void addMissToStudent(){
        Misses m = null;
        SimpleDateFormat displayedFormat = new SimpleDateFormat(DateConverter.DATE_MY, Locale.getDefault());
        if(binding2.sickLay.sDate.getText().toString().isEmpty() || binding2.sickLay.eDate.getText().toString().isEmpty()){
            showError("Заполните все поля.\nВведите дату в формате: \'"+DateConverter.DATE_MY+"\'");
            return;
        }
        Date sDate, eDate;
        try {
            sDate = displayedFormat.parse(binding2.sickLay.sDate.getText().toString());
            eDate = displayedFormat.parse(binding2.sickLay.eDate.getText().toString());

        } catch (ParseException e) {
            showError("Ввелён неверный формат даты.\nВведите дату в формате: \'"+DateConverter.DATE_MY+"\'");
            e.printStackTrace();
            return;
        }
        if(!validateTimeRange(sDate, eDate))
            return;
        m = new Misses(sDate, eDate);
        if(m != null)
            sicknessRecViewAdapter.addItem(m);
    }

    boolean validateTimeRange(Date sDate, Date eDate){
        Calendar validsDate = Calendar.getInstance();
        validsDate.set(2000, 1, 1);
        Calendar valideDate = Calendar.getInstance();
        valideDate.set(3000, 1, 1);
        if(!sDate.after(validsDate.getTime()) || !eDate.after(validsDate.getTime()) || !sDate.before(valideDate.getTime()) || !eDate.before(valideDate.getTime())){
            String dateStart= "01.01.2000", dateEnd = "01.01.3000";
            showError("Введена некорректная дата диапозон дат должен быть : "+
                    dateStart + "-" + dateEnd );
            return false;
        }
        return true;
    }



    //true for enable edit
    void canEdit(boolean isEditable){
        //getView().clearFocus();
        androidx.constraintlayout.widget.Group group = new Group(this);

        if(!isEditable){
            binding2.commonLay.studentSNP.setInputType(InputType.TYPE_NULL);
            binding2.commonLay.studentBirth.setInputType(InputType.TYPE_NULL);
            binding2.commonLay.studentYears.setInputType(InputType.TYPE_NULL);
            binding2.commonLay.studentEmail.setInputType(InputType.TYPE_NULL);
            binding2.commonLay.studentPhone.setInputType(InputType.TYPE_NULL);
            //Адресс проживания
            binding2.commonLay.ResAddress.setInputType(InputType.TYPE_NULL);
            binding2.commonLay.ResAddress.setLines(3);
            binding2.commonLay.ResAddress.setHorizontallyScrolling(false);
            //Проживает в общежитии
            binding2.commonLay.dormitoryCheckBox.setClickable(false);

            //инн
            binding2.medLay.studentITN.setInputType(InputType.TYPE_NULL);
            //снилс
            binding2.medLay.studentINOILA.setInputType(InputType.TYPE_NULL);
            //полис
            binding2.medLay.studentIMPNSSeria.setInputType(InputType.TYPE_NULL);
            binding2.medLay.studentIMPNSNumber.setInputType(InputType.TYPE_NULL);

            //caretakertLay
            binding2.caretakertLay.FatherSNP.setInputType(InputType.TYPE_NULL);
            binding2.caretakertLay.FatherNumber.setInputType(InputType.TYPE_NULL);

            //паспортные данные
            binding2.passLay.studentPassportSeria.setInputType(InputType.TYPE_NULL);
            binding2.passLay.studentPassportNumber.setInputType(InputType.TYPE_NULL);
            binding2.passLay.subdivisionCode.setInputType(InputType.TYPE_NULL);
            binding2.passLay.IssuingAuthority.setInputType(InputType.TYPE_NULL);
            binding2.passLay.IssueDate.setInputType(InputType.TYPE_NULL);
            //Адресс регистрации
            binding2.passLay.Address.setInputType(InputType.TYPE_NULL);
            binding2.passLay.Address.setLines(3);
            binding2.passLay.Address.setHorizontallyScrolling(false);


            //Сканы
            binding2.sickLay.sDate.setVisibility(View.GONE);
            binding2.sickLay.sDate.setInputType(InputType.TYPE_NULL);
            binding2.sickLay.eDate.setVisibility(View.GONE);
            binding2.sickLay.eDate.setInputType(InputType.TYPE_NULL);
            binding2.sickLay.addMiss.setVisibility(View.GONE);
            binding2.sickLay.addScan.setVisibility(View.GONE);

        } else{
            binding2.commonLay.studentSNP.setInputType(InputType.TYPE_TEXT_FLAG_AUTO_CORRECT);
            binding2.commonLay.studentBirth.setInputType(InputType.TYPE_DATETIME_VARIATION_DATE);
            binding2.commonLay.studentEmail.setInputType(InputType.TYPE_TEXT_VARIATION_WEB_EMAIL_ADDRESS|InputType.TYPE_TEXT_FLAG_AUTO_CORRECT|InputType.TYPE_TEXT_FLAG_AUTO_COMPLETE);
            binding2.commonLay.studentPhone.setInputType(InputType.TYPE_CLASS_PHONE);

            //Адресс проживания
            binding2.commonLay.ResAddress.setInputType(InputType.TYPE_TEXT_FLAG_AUTO_COMPLETE|InputType.TYPE_TEXT_FLAG_MULTI_LINE);
            binding2.commonLay.ResAddress.setLines(3);
            binding2.commonLay.ResAddress.setHorizontallyScrolling(false);

            //Проживает в общежитии
            binding2.commonLay.dormitoryCheckBox.setClickable(true);

            //инн
            binding2.medLay.studentITN.setInputType(InputType.TYPE_TEXT_FLAG_AUTO_COMPLETE);
            //снилс
            binding2.medLay.studentINOILA.setInputType(InputType.TYPE_TEXT_FLAG_AUTO_COMPLETE);
            //полис
            binding2.medLay.studentIMPNSSeria.setInputType(InputType.TYPE_TEXT_FLAG_AUTO_COMPLETE);
            binding2.medLay.studentIMPNSNumber.setInputType(InputType.TYPE_TEXT_FLAG_AUTO_COMPLETE);

            //caretakertLay
            binding2.caretakertLay.FatherSNP.setInputType(InputType.TYPE_TEXT_FLAG_AUTO_COMPLETE);
            binding2.caretakertLay.FatherNumber.setInputType(InputType.TYPE_TEXT_FLAG_AUTO_COMPLETE);

            //паспортные данные
            binding2.passLay.studentPassportSeria.setInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL);
            binding2.passLay.studentPassportNumber.setInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL);
            binding2.passLay.subdivisionCode.setInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL|InputType.TYPE_TEXT_FLAG_AUTO_COMPLETE);
            binding2.passLay.IssuingAuthority.setInputType(InputType.TYPE_TEXT_FLAG_AUTO_COMPLETE);
            binding2.passLay.IssueDate.setInputType(InputType.TYPE_DATETIME_VARIATION_DATE);
            //Адресс регистрации
            binding2.passLay.Address.setInputType(InputType.TYPE_TEXT_FLAG_AUTO_COMPLETE|InputType.TYPE_TEXT_FLAG_MULTI_LINE);
            binding2.passLay.Address.setLines(3);
            binding2.passLay.Address.setHorizontallyScrolling(false);

            //Сканы
            binding2.sickLay.sDate.setVisibility(View.VISIBLE);
            binding2.sickLay.sDate.setInputType(InputType.TYPE_DATETIME_VARIATION_DATE);
            binding2.sickLay.eDate.setVisibility(View.VISIBLE);
            binding2.sickLay.eDate.setInputType(InputType.TYPE_DATETIME_VARIATION_DATE);
            binding2.sickLay.addMiss.setVisibility(View.VISIBLE);
            binding2.sickLay.addScan.setVisibility(View.VISIBLE);
        }

        if(isEditable || binding2.commonLay.studentPhone.getText().toString().isEmpty()){
            binding2.commonLay.studentPhone.setOnClickListener(null);
        } else {
            binding2.commonLay.studentPhone.setOnClickListener(makeCall());
            binding2.commonLay.studentPhone.setTextColor(getResources().getColorStateList(R.color.editable, getTheme()));
        }
        if(isEditable || binding2.commonLay.studentEmail.getText().toString().isEmpty()){
            binding2.commonLay.studentEmail.setOnClickListener(null);
        } else {
            binding2.commonLay.studentEmail.setOnClickListener(makeMail());
            binding2.commonLay.studentEmail.setTextColor(getResources().getColorStateList(R.color.editable, getTheme()));
        }


    }

    private int getAge(@NonNull Date birthday)
    {
        GregorianCalendar today = new GregorianCalendar();
        GregorianCalendar bday = new GregorianCalendar();
        GregorianCalendar bdayThisYear = new GregorianCalendar();

        bday.setTime(birthday);
        bdayThisYear.setTime(birthday);
        bdayThisYear.set(Calendar.YEAR, today.get(Calendar.YEAR));

        int age = today.get(Calendar.YEAR) - bday.get(Calendar.YEAR);

        if(today.getTimeInMillis() < bdayThisYear.getTimeInMillis())
            age--;

        return age;
    }




    @SuppressLint("NotifyDataSetChanged")
    void initRecView(){
        sicknessRecViewAdapter = new SicknessRecViewAdapter();
        sicknessRecView.setHasFixedSize(false);
        sicknessRecView.setAdapter(sicknessRecViewAdapter);
        sicknessRecViewAdapter.notifyDataSetChanged();
    }

    View.OnClickListener makeCall(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent callIntent = new Intent(Intent.ACTION_DIAL);
                String number =((EditText)v).getText().toString();
                callIntent.setData(Uri.parse("tel:"+number));
                callIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(callIntent);
            }
        };
    }
    View.OnClickListener makeMail(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText editText = (EditText)v;
                String emails = editText.getText().toString();
                Intent intent = new Intent(Intent.ACTION_SENDTO);
                intent.setData(Uri.parse("mailto:"+emails)); // only email apps should handle this
                intent.putExtra(Intent.EXTRA_EMAIL, emails);
                /*if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
                    startActivity(intent);
                }*/
                //intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(Intent.createChooser(intent, "Send email..."));
            }
        };
    }

    public void showError(String msg){
        alertDialog(msg).show();
    }
    AlertDialog.Builder alertDialog(String msg) {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("Ошибка");
        alert.setMessage(msg);
        alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
            }
        });
        return alert;
    }

    private void mockLoadStudentInfo(Person person){
        binding2.commonLay.studentSNP.setText(person.getSNP());
        binding2.commonLay.studentEmail.setText(person.getEmail());
        binding2.commonLay.studentPhone.setText(person.getPhone());
        studentData = DataGenerator.mockGetStudent(person);
        completeFields(studentData);
        canEdit(false);
    }
    private void loadStudentInfo(Person person){
        if(person == null) {
            showError(getString(R.string.error));
            return;
        }
        binding2.loading.setVisibility(View.VISIBLE);
        binding2.commonLay.getRoot().setVisibility(View.GONE);
        binding2.medLay.getRoot().setVisibility(View.GONE);
        binding2.passLay.getRoot().setVisibility(View.GONE);
        binding2.caretakertLay.getRoot().setVisibility(View.GONE);
        binding2.sickLay.getRoot().setVisibility(View.GONE);
        ApiService.getInstance().getApi().getStudentDataById(personID).enqueue(new Callback<StudentData>() {
            @Override
            public void onResponse(Call<StudentData> call, Response<StudentData> response) {
                if (response.isSuccessful() && response.body() != null) {
                    studentData =response.body();
                }
                else showError(getString(R.string.error));
            }

            @Override
            public void onFailure(Call<StudentData> call, Throwable t) {
                t.printStackTrace();
                showError(getString(R.string.error));
            }
        });
        if(studentData == null) return;
        completeFields(studentData);
        canEdit(false);
        binding2.loading.setVisibility(View.GONE);
        binding2.commonLay.getRoot().setVisibility(View.VISIBLE);
        binding2.medLay.getRoot().setVisibility(View.VISIBLE);
        binding2.passLay.getRoot().setVisibility(View.VISIBLE);
        binding2.caretakertLay.getRoot().setVisibility(View.VISIBLE);
        binding2.sickLay.getRoot().setVisibility(View.VISIBLE);
    }
    public void show(View view) {
        View view2 = view.getRootView();
        RelativeLayout layout = null;
        switch (view.getId()) {
            case R.id.commonInf:
                layout = (RelativeLayout) view2.findViewById(R.id.expandable);
                break;
            case R.id.medInf:
                layout = (RelativeLayout) view2.findViewById(R.id.expandable2);
                break;
            case R.id.documents:
                layout = (RelativeLayout) view2.findViewById(R.id.expandable3);
                break;
            case R.id.family:
                layout = (RelativeLayout) view2.findViewById(R.id.expandable4);
                break;
            case R.id.sicknessСertificates:
                layout = (RelativeLayout) view2.findViewById(R.id.expandable5);
                break;
            default: {
            }
            break;
        }
        if (layout != null) {
            TextView t = ((TextView)view);
            if(layout.getVisibility() == View.GONE)
                t.setText(t.getText().toString().replace("▼", "▲"));
            else
                t.setText(t.getText().toString().replace("▲", "▼"));
            TransitionManager.beginDelayedTransition(layout, new AutoTransition());
            layout.setVisibility(layout.getVisibility() == View.GONE ? View.VISIBLE : View.GONE);
        }
    }

    void mockLoadMisses(){
        Misses m = new Misses();
        Calendar c = Calendar.getInstance();
        c.set(2022, 2, 15);
        m.IllnessDate = c.getTime();
        m.RecoveryDate = new Date(c.getTimeInMillis()+604800000);
        List<Misses> ml = new ArrayList<Misses>();
        ml.add(m);
        m = new Misses();
        c.set(2022, 2, 25);
        m.IllnessDate = c.getTime();
        m.RecoveryDate = new Date(c.getTimeInMillis()+604800000);
        ml.add(m);
        sicknessRecViewAdapter.setMissesList(ml);
    }

    private void loadMissesInfo(Person person){
        if(person == null) {
            showError(getString(R.string.error));
            return;
        }
        //FIXME Добавить загрузку списка справок
        // ApiService.getInstance().getApi().
        //Fixme mock
        if(studentData == null) return;


    }

    void updateStudentData(StudentData studentData){
        String name, surename, patronymic;
        surename = binding2.commonLay.studentSNP.getText().toString().split(" ",3)[0];
        name = binding2.commonLay.studentSNP.getText().toString().split(" ",3)[1];
        patronymic = binding2.commonLay.studentSNP.getText().toString().split(" ",3)[2];
        studentData.getPerson().setSurname(name);
        studentData.getPerson().setName(name);
        studentData.getPerson().setPatronymic(name);
        Date date = null;
        try {
            date = new SimpleDateFormat(DateConverter.DATE_MY).parse(binding2.commonLay.studentBirth.getText().toString());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        studentData.setBirthday(date);
        studentData.getPerson().setEmail(binding2.commonLay.studentEmail.getText().toString());
        studentData.getPerson().setPhone(binding2.commonLay.studentPhone.getText().toString());
        //FIXME адресса изменять нельзя
        studentData.setDormitory(binding2.commonLay.dormitoryCheckBox.isChecked());
        //инн
        studentData.setiTN(binding2.medLay.studentITN.getText().toString());
        //снилс
        studentData.setInsurPolicy(binding2.medLay.studentINOILA.getText().toString());
        //полис
        studentData.setMedPolicy(binding2.medLay.studentIMPNSSeria.getText().toString()+
                binding2.medLay.studentIMPNSNumber.getText().toString());

        //опекуны
        Person careTaker = null;
        if(!studentData.getPerens().isEmpty())
            careTaker = studentData.getPerens().get(0);
        if(careTaker != null){
            surename = binding2.caretakertLay.FatherSNP.getText().toString().split(" ",3)[0];
            name = binding2.caretakertLay.FatherSNP.getText().toString().split(" ",3)[1];
            patronymic = binding2.caretakertLay.FatherSNP.getText().toString().split(" ",3)[2];
            careTaker.setSurname(surename);
            careTaker.setName(name);
            careTaker.setPatronymic(patronymic);
            careTaker.setPhone(binding2.caretakertLay.FatherNumber.getText().toString());
        }
        //паспортные данные
        studentData.getPassport().setSeries(binding2.passLay.studentPassportSeria.getText().toString());
        studentData.getPassport().setNumber(binding2.passLay.studentPassportNumber.getText().toString());
        studentData.getPassport().setSubdivisionCode(binding2.passLay.subdivisionCode.getText().toString());
        studentData.getPassport().setIssuingAuthority(binding2.passLay.IssuingAuthority.getText().toString());
        try {
            date = new SimpleDateFormat(DateConverter.DATE_MY).parse(binding2.passLay.IssueDate.getText().toString());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        studentData.getPassport().setIssueDate(date);
        //FIXME адресса изменять нельзя
        //Адресс регистрации
    }

}