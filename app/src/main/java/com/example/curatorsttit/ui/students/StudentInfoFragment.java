package com.example.curatorsttit.ui.students;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.Group;
import androidx.core.view.MenuProvider;
import androidx.fragment.app.Fragment;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

import com.example.curatorsttit.R;
import com.example.curatorsttit.adapters.SicknessRecViewAdapter;
import com.example.curatorsttit.adapters.StudentsRecViewAdapter;
import com.example.curatorsttit.common.DataGenerator;
import com.example.curatorsttit.common.DateConverter;
import com.example.curatorsttit.databinding.FragmentStudentInfoBinding;
import com.example.curatorsttit.databinding.FragmentStudentInfoTwoBinding;
import com.example.curatorsttit.models.Misses;
import com.example.curatorsttit.models.Person;
import com.example.curatorsttit.models.Student;
import com.example.curatorsttit.models.StudentData;
import com.example.curatorsttit.network.ApiService;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StudentInfoFragment extends Fragment {
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
        super.onCreate(savedInstanceState);
        if(getArguments() !=  null)
            personID = getArguments().getInt("personID", -1);
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();
        person = gson.fromJson( getArguments().getString("person"),Person.class);
        setHasOptionsMenu(true);
    }

    @Override
    public void onStart() {
        super.onStart();
        toolbar = (Toolbar) getView().findViewById(R.id.toolbar);//.findViewById(R.id.toolbar);
        //toolbar.setNavigationIcon(R.drawable.ic_arrow_back);
        toolbar.setNavigationIcon(androidx.appcompat.R.drawable.abc_ic_ab_back_material);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (view.getId()) {
                    case android.R.id.home:
                        getActivity().onBackPressed();
                        break;
                    case -1:
                        getActivity().onBackPressed();
                        break;
                    case android.R.id.accessibilitySystemActionBack:
                        getActivity().onBackPressed();
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
                    canEdit(false);
                    return onOptionsItemSelected(menuItem);
                });
            }

            @Override
            public boolean onMenuItemSelected(@NonNull MenuItem menuItem) {
                return false;
            }
        });
        mockLoadStudentInfo(person);
    }
    void clearEdit(){
        getView().clearFocus();
        completeFields(DataGenerator.mockGetStudent(person));
    }
    void completeFields(StudentData studentData){
        binding2.commonLay.studentSNP.setText(studentData.getPerson().getSNP());
        binding2.commonLay.studentBirth.setText(studentData.getBirthday().toString());
        binding2.commonLay.studentBirth.setText(
            DateConverter.getNyFormattedDate(studentData.getBirthday().toString())
        );
        binding2.commonLay.studentYears.setText(String.valueOf(getAge(studentData.getBirthday())));
        binding2.commonLay.studentEmail.setText(studentData.getPerson().getEmail());
        binding2.commonLay.studentPhone.setText(studentData.getPerson().getPhone());

        //Адресс проживания
        binding2.commonLay.ResAddress.setText(studentData.getResidentialAddress().getAddressAsString());

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
    }

    @Override
    public void onResume() {
        super.onResume();
        alertDialog("Вызвано сотстояние Resume").setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {

            }
        }).show();
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
                        try (InputStream imageStream = getActivity().getContentResolver().openInputStream(selectedImage)) {
                            Bitmap yourSelectedImage = BitmapFactory.decodeStream(imageStream);
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        Misses m = new Misses();
                        Calendar c = Calendar.getInstance();
                        //fixme
                        c.set(2022, 2, 15);
                        m.IllnessDate = c.getTime();
                        c.set(2022, 2, 25);
                        List<Misses> ml = new ArrayList<Misses>();
                        ml.add(m);
                        sicknessRecViewAdapter.addItems(ml);
                    }
                }
            });



    //true for enable edit
    void canEdit(boolean isEditable){
        getView().clearFocus();
        androidx.constraintlayout.widget.Group group = new Group(requireContext());

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
            binding2.commonLay.studentPhone.setTextColor(getResources().getColorStateList(R.color.editable, getActivity().getTheme()));
        }
        if(isEditable || binding2.commonLay.studentEmail.getText().toString().isEmpty()){
            binding2.commonLay.studentEmail.setOnClickListener(null);
        } else {
            binding2.commonLay.studentEmail.setOnClickListener(makeMail());
            binding2.commonLay.studentEmail.setTextColor(getResources().getColorStateList(R.color.editable, getActivity().getTheme()));
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentStudentInfoBinding.inflate(inflater, container, false);
        binding2 = FragmentStudentInfoTwoBinding.inflate(inflater, container, false);
        View view = binding2.getRoot();

        //FixME нормальную инициализацию скана
        /*------------------------------------*/
        sicknessRecView = view.findViewById(R.id.recyclerMisses);
        initRecView();
        /*------------------------------------*/
        return view;
    }


    @SuppressLint("NotifyDataSetChanged")
    void initRecView(){
        //fixME
        Misses m = new Misses();
        Calendar c = Calendar.getInstance();
        c.set(2022, 2, 15);
        m.IllnessDate = c.getTime();
        c.set(2022, 2, 25);
        m.RecoveryDate = c.getTime();
        List<Misses> ml = new ArrayList<Misses>();
        ml.add(m);
        ml.add(m);
        sicknessRecViewAdapter = new SicknessRecViewAdapter(ml, new SicknessRecViewAdapter.onMissListener() {
            @Override
            public void onMissClick(Misses misses) {
                //FixME скачивание скана

            }
        });
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
        AlertDialog.Builder alert = new AlertDialog.Builder(requireContext());
        alert.setTitle("Ошибка");
        alert.setMessage(msg);
        alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                getActivity().onBackPressed();
            }
        });
        return alert;
    }

    private void mockLoadStudentInfo(Person person){
        binding2.commonLay.studentSNP.setText(person.getSNP());
        binding2.commonLay.studentEmail.setText(person.getEmail());
        binding2.commonLay.studentPhone.setText(person.getPhone());
        completeFields(DataGenerator.mockGetStudent(person));
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

}