package com.example.introduccionconstraintlayout.ui;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.example.introduccionconstraintlayout.NuevaNotaDialogFragment;
import com.example.introduccionconstraintlayout.NuevaNotaDialogViewModel;
import com.example.introduccionconstraintlayout.R;
import com.example.introduccionconstraintlayout.db.entity.NotaEntity;

import java.util.ArrayList;
import java.util.List;



public class NotaFragment extends Fragment {

    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    // TODO: Customize parameters
    private int mColumnCount = 2;
    private List<NotaEntity> notaList;
    private MyNotaRecyclerViewAdapter adapterNotas;
    private NuevaNotaDialogViewModel notaViewModel;
    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public NotaFragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static NotaFragment newInstance(int columnCount) {
        NotaFragment fragment = new NotaFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }

        //Indicamos que el fragmento tiene un menu de opciones propio
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_nota_list, container, false);

        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;
            if(view.getId()==R.id.listPortrait){

                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
                float dpWidth = displayMetrics.widthPixels / displayMetrics.density;
                int numeroColumna = (int)(dpWidth / 180);
                recyclerView.setLayoutManager(new StaggeredGridLayoutManager( numeroColumna, StaggeredGridLayoutManager.VERTICAL));
            }

            notaList = new ArrayList<>();
//            notaList.add(new NotaEntity("Lista de compra", "Comprar pan tostado",true, android.R.color.holo_blue_light));
//            notaList.add(new NotaEntity("Recordar", "he aparcado el coche en la calle almagro no olvidar pagar el parqumetr a la señora maria castro",false ,android.R.color.holo_green_light));
//            notaList.add(new NotaEntity("Cumpleaños (fiesta)", "No olvidar las velas",true,android.R.color.holo_orange_light));
//
            adapterNotas = new MyNotaRecyclerViewAdapter(notaList, getActivity());
            recyclerView.setAdapter(adapterNotas);

            lanzarViewModel();
        }

        return view;
    }

    private void lanzarViewModel() {
        notaViewModel = ViewModelProviders.of(getActivity())
                .get(NuevaNotaDialogViewModel.class);
        notaViewModel.getAllNotas().observe(getActivity(), new Observer<List<NotaEntity>>() {
            @Override
            public void onChanged(@Nullable List<NotaEntity> notaEntities) {
                adapterNotas.setNuevasNotas(notaEntities);
            }
        });
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.options_menu_nota_fragment, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_add_nota:
                mostrarDialogoNuevaNota();
                return true;
                default:
                    return super.onOptionsItemSelected(item);
        }
    }

    private void mostrarDialogoNuevaNota() {
        FragmentManager fm = getActivity().getSupportFragmentManager();
        NuevaNotaDialogFragment dialognuevaNota = new NuevaNotaDialogFragment();
        dialognuevaNota.show(fm, "NuevaNotaDialogFragment");
    }
}
