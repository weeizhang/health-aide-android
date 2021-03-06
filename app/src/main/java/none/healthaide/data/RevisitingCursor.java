package none.healthaide.data;

import android.database.Cursor;
import android.database.CursorWrapper;

import none.healthaide.data.HealthAidContract.MedicalRecordsEntry;
import none.healthaide.data.HealthAidContract.RevisitingEventEntry;

public class RevisitingCursor extends CursorWrapper {
    private Cursor cursor;
    public RevisitingCursor(Cursor cursor) {
        super(cursor);
        this.cursor = cursor;
    }

    public String getRevisitingDate() {
        int columnIndex = cursor.getColumnIndex(RevisitingEventEntry.COLUMN_NAME_REVISITING_DATE);
        return getString(columnIndex);
    }

    public String getTitle() {
        int columnIndex = cursor.getColumnIndex(MedicalRecordsEntry.COLUMN_NAME_TITLE);
        return getString(columnIndex);
    }

    public String getStartDate() {
        int columnIndex = cursor.getColumnIndex(MedicalRecordsEntry.COLUMN_NAME_START_DATE);
        return getString(columnIndex);
    }

    public String getEndDate() {
        int columnIndex = cursor.getColumnIndex(MedicalRecordsEntry.COLUMN_NAME_END_DATE);
        return getString(columnIndex);
    }

    public String getCaseDescribe() {
        int columnIndex = cursor.getColumnIndex(MedicalRecordsEntry.COLUMN_NAME_DESCRIPTION);
        return getString(columnIndex);
    }

    public String getCaseType() {
        int columnIndex = cursor.getColumnIndex(MedicalRecordsEntry.COLUMN_NAME_TYPE);
        return getString(columnIndex);
    }

    public String getCureDescription() {
        int columnIndex = cursor.getColumnIndex(MedicalRecordsEntry.COLUMN_NAME_CURE_DESCRIPTION);
        return getString(columnIndex);
    }

    public String getHospital() {
        int columnIndex = cursor.getColumnIndex(MedicalRecordsEntry.COLUMN_NAME_HOSPITAL);
        return getString(columnIndex);
    }

    public String getDoctor() {
        int columnIndex = cursor.getColumnIndex(MedicalRecordsEntry.COLUMN_NAME_DOCTOR);
        return getString(columnIndex);
    }
}
