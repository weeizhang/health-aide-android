package none.healthaide.revisting;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.common.collect.Lists;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import none.healthaide.HealthAidApplication;
import none.healthaide.R;
import none.healthaide.data.HealthAidContract;
import none.healthaide.data.RevisitingCursor;
import none.healthaide.model.Case;

public class RevisitingFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    public static final String TAG = RevisitingFragment.class.getSimpleName();

    private static final int REVISITING_EVENT_LOADER = 0;

    private Unbinder unbinder;
    @BindView(R.id.toolbar_actionbar)
    Toolbar toolbar;
    @BindView(R.id.revisiting_event_list_view)
    RecyclerView revisitingRecyclerView;

    private RevisitingListViewAdapter revisitingListViewAdapter;
    private List<Case> caseList = Lists.newArrayList();

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        ((HealthAidApplication) getActivity().getApplication()).getComponent().inject(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_revisiting, container, false);
        unbinder = ButterKnife.bind(this, view);

        createRevisitingEventView();

        getLoaderManager().initLoader(REVISITING_EVENT_LOADER, null, this);

        return view;
    }

    private void createRevisitingEventView() {
        revisitingRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        revisitingListViewAdapter = new RevisitingListViewAdapter(getActivity(), caseList);
        revisitingRecyclerView.setAdapter(revisitingListViewAdapter);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initActionBar();
    }

    private void initActionBar() {
        setHasOptionsMenu(true);
        toolbar.setTitle(getResources().getString(R.string.revisiting_event));
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        switch (id) {
            case REVISITING_EVENT_LOADER:
                return new CursorLoader(
                        getActivity(), HealthAidContract.REVISITING_EVENT_TABLE_CONTENT_URI, null, null, null, null);
            default:
                return null;
        }
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if (data != null && data.moveToFirst()) {
            caseList.clear();
            do {
                RevisitingCursor revisitingCursor = new RevisitingCursor(data);
                caseList.add(new Case()
                        .setRevisitingDate(revisitingCursor.getRevisitingDate())
                        .setTitle(revisitingCursor.getTitle())
                        .setStartDate(revisitingCursor.getStartDate())
                        .setCaseDescribe(revisitingCursor.getCaseDescribe())
                        .setHospital(revisitingCursor.getHospital())
                        .setDoctor(revisitingCursor.getDoctor()));
            } while (data.moveToNext());

            revisitingListViewAdapter.setCaseList(caseList);
            revisitingListViewAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
}