package none.healthaide.usermedicalrecords;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;

import org.joda.time.DateTime;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import none.healthaide.HealthAidApplication;
import none.healthaide.R;
import none.healthaide.model.MedicalRecords;
import none.healthaide.utils.DateUtil;

public class NewMedicalRecordsFragment extends Fragment implements NewMedicalRecordsView {
    public static final String TAG = NewMedicalRecordsFragment.class.getSimpleName();

    private static final int SELECT_PICTURE = 1;

    private Unbinder unbinder;

    @BindView(R.id.toolbar_actionbar)
    Toolbar toolbar;
    @BindView(R.id.input_title)
    EditText titleEditText;
    @BindView(R.id.input_start_date)
    EditText startDateEditText;
    @BindView(R.id.input_end_date)
    EditText endDateEditText;
    @BindView(R.id.input_medical_records_describe)
    EditText caseDescribeEditText;
    @BindView(R.id.input_medical_records_type)
    EditText caseTypeEditText;
    @BindView(R.id.input_cure_description)
    EditText cureDescriptionEditText;
    @BindView(R.id.input_hospital)
    EditText hospitalEditText;
    @BindView(R.id.input_doctor)
    EditText doctorEditText;
    @BindView(R.id.medical_records_image_view)
    ImageView uploadCaseImage;

    private Uri photoUri;
    private NewMedicalRecordsPresenter newMedicalRecordsPresenter;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        ((HealthAidApplication) getActivity().getApplication()).getComponent().inject(this);
        newMedicalRecordsPresenter = new NewMedicalRecordsPresenter(getContext().getContentResolver(), this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_new_case, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initActionBar();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
        inflater.inflate(R.menu.new_case_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                getActivity().onBackPressed();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == SELECT_PICTURE) {
            if (resultCode == Activity.RESULT_OK) {
                photoUri = data.getData();
                uploadCaseImage.setImageURI(photoUri);
            }
        }
    }

    @OnClick(R.id.input_start_date)
    public void startDateOnClick() {
        createDatePickerDialog(startDateEditText).show();
    }

    @OnClick(R.id.input_end_date)
    public void endDateOnClick() {
        createDatePickerDialog(endDateEditText).show();
    }

    @OnClick(R.id.medical_records_image_view)
    public void uploadMedicalRecordsOnClick() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,
                getString(R.string.select_picture)), SELECT_PICTURE);
    }

    @OnClick(R.id.new_medical_records_submit_button)
    public void newMedicalRecordsSubmitOnClick() {
        View view = getActivity().getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
        newMedicalRecordsPresenter.submitNewCase();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    private void initActionBar() {
        setHasOptionsMenu(true);
        toolbar.setTitle(getResources().getString(R.string.new_medical_records));
//        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
//        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    @NonNull
    private DatePickerDialog createDatePickerDialog(final EditText editText) {
        return new DatePickerDialog(
                getActivity(),
                R.style.DialogTheme,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                        editText.setText(DateUtil.dateToString(year, month + 1, day));
                    }
                },
                DateTime.now().getYear(), DateTime.now().getMonthOfYear() - 1, DateTime.now().getDayOfMonth());
    }

    @Override
    public MedicalRecords getMedicalRecords() {
        return new MedicalRecords()
                .setTitle(titleEditText.getText().toString())
                .setStartDate(startDateEditText.getText().toString())
                .setEndDate(endDateEditText.getText().toString())
                .setMedicalRecordsDescribe(caseDescribeEditText.getText().toString())
                .setMedicalRecordsType(caseTypeEditText.getText().toString())
                .setCureDescription(cureDescriptionEditText.getText().toString())
                .setHospital(hospitalEditText.getText().toString())
                .setDoctor(doctorEditText.getText().toString())
                .setPhotoUriStr(photoUri == null ? null : photoUri.toString());
    }

    @Override
    public void loadSuccess(long id) {
        getActivity().getSupportFragmentManager().popBackStack();
    }

    @Override
    public void loadFailed() {

    }
}