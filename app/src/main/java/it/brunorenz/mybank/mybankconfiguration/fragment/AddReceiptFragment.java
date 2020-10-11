package it.brunorenz.mybank.mybankconfiguration.fragment;

import android.app.DatePickerDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import it.brunorenz.mybank.mybankconfiguration.MyBankIntents;
import it.brunorenz.mybank.mybankconfiguration.R;
import it.brunorenz.mybank.mybankconfiguration.bean.BankAccount;
import it.brunorenz.mybank.mybankconfiguration.bean.Category;
import it.brunorenz.mybank.mybankconfiguration.bean.GenericDataContainer;
import it.brunorenz.mybank.mybankconfiguration.httpservice.MyBankServerManager;

public class AddReceiptFragment extends Fragment {

    private static final String TAG = AddReceiptFragment.class.getSimpleName();
    private Spinner elencoBanche;
    private ArrayAdapter<String> elencoBancheAdapter;
    private Spinner elencoCategorie;
    private ArrayAdapter<String> elencoCategorieAdapter;
    private EditText importo;
    private EditText dataScontrino;
    private MyBankServerManager serverManager;
    private Movimento movimento;
    private List<BankAccount> accounts;
    private List<Category> categories;

    private String initialAccount;

    public void setInitialAccount(String initialAccount) {
        this.initialAccount = initialAccount;
    }

    public List<Category> getCategories() {
        if (categories == null) categories = new ArrayList<>();
        return categories;
    }

    public List<BankAccount> getAccounts() {
        if (accounts == null) accounts = new ArrayList<>();
        return accounts;
    }

    private class Movimento {
        BigDecimal importo;
        Date data;
        Category categoria;
        BankAccount banca;

        public BigDecimal getImporto() {
            return importo;
        }

        public void setImporto(BigDecimal importo) {
            this.importo = importo;
        }

        public Date getData() {
            return data;
        }

        public void setData(Date data) {
            this.data = data;
        }

        public Category getCategoria() {
            return categoria;
        }

        public void setCategoria(Category categoria) {
            this.categoria = categoria;
        }

        public BankAccount getBanca() {
            return banca;
        }

        public void setBanca(BankAccount banca) {
            this.banca = banca;
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {

        //
        movimento = new Movimento();
        // register reciever
        final IntentFilter filter = new IntentFilter();
        filter.addAction(MyBankIntents.DATA_GETCFG_RESPONSE);
        getContext().registerReceiver(createBroadcastReceiver(), filter);
        serverManager = MyBankServerManager.createMyBankServerManager(getContext());
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        View v = inflater.inflate(R.layout.fragment_add_receipt, container, false);
        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        elencoBanche = (Spinner) view.findViewById(R.id.elencoCarte);
        elencoBancheAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item);
        elencoBancheAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        elencoBanche.setAdapter(elencoBancheAdapter);
        elencoBanche.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // selectect
                Log.d(TAG, "Selected " + position);
                movimento.setBanca(getAccounts().get(position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Log.d(TAG, "onNothingSelected ");
            }
        });

        elencoCategorie = (Spinner) view.findViewById(R.id.elencoCategorie);
        elencoCategorieAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item);
        elencoCategorieAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        elencoCategorie.setAdapter(elencoCategorieAdapter);
        elencoCategorie.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // selectect
                Log.d(TAG, "Selected " + position);
                movimento.setCategoria(getCategories().get(position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Log.d(TAG, "onNothingSelected ");
            }
        });

        importo = (EditText) view.findViewById(R.id.importoScontrino);
        manageImporto(importo);

        dataScontrino = (EditText) view.findViewById(R.id.dataScontrino);
        manageDataScontrino(dataScontrino);

        manageElencoBanche();
    }

    private String formatImporto(String val) {
        String out = "";
        try {
            if (val != null && val.length() > 0) {
                BigDecimal d = new BigDecimal(val);
                movimento.setImporto(d);
                out = String.format(Locale.ITALY, "%.2f €", d);
                Log.d(TAG, "Importo : " + out);
            }
        } catch (Exception e) {
            Log.e(TAG, "Erorre conversione ", e);
        }
        return out;
    }

    private void updateLabel(Calendar myCalendar) {
        String myFormat = "MM/dd/yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.ITALY);

        dataScontrino.setText(sdf.format(myCalendar.getTime()));
    }

    private void manageDataScontrino(EditText dataScontrino) {
        if (dataScontrino != null) {
            final Calendar myCalendar = Calendar.getInstance();
            // set initial value
            movimento.setData(myCalendar.getTime());
            updateLabel(myCalendar);
            DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                    myCalendar.set(Calendar.YEAR, year);
                    myCalendar.set(Calendar.MONTH, month);
                    myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                    movimento.setData(myCalendar.getTime());
                    updateLabel(myCalendar);
                }
            };

            dataScontrino.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new DatePickerDialog(getContext(), date, myCalendar
                            .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                            myCalendar.get(Calendar.DAY_OF_MONTH)).show();
                }
            });
        }
    }

    private void manageImporto(EditText importo) {
        if (importo != null) {
            importo.setOnKeyListener(new View.OnKeyListener() {
                @Override
                public boolean onKey(View v, int keyCode, KeyEvent event) {
                    Log.d(TAG, ">>> Event " + event.toString());
                    if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                            (keyCode == KeyEvent.KEYCODE_ENTER)) {
                        String val = importo.getText().toString();
                        importo.setText(formatImporto(val));
                        return false;
                    }
                    return false;
                }
            });
            importo.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    Log.d(TAG, ">>> onFocusChange " + hasFocus);
                    if (hasFocus) {
                        if (movimento.getImporto() != null) {
                            String out = String.format(Locale.ITALY, "%.2f", movimento.getImporto());
                            importo.setText(out);
                        }
                    } else {
                        String val = importo.getText().toString();
                        if (!val.contains("€"))
                            importo.setText(formatImporto(val));
                    }
                }
            });

            importo.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    Log.d(TAG, ">>> onTouch " + event.toString());
                    if (event.getAction() == KeyEvent.ACTION_DOWN) {
                        if (movimento.getImporto() != null) {
                            String out = String.format(Locale.ITALY, "%.2f", movimento.getImporto());
                            importo.setText(out);
                        }
                    }
                    return false;
                }
            });


        }
    }

    private void manageElencoBanche() {
        if (serverManager != null) {
            serverManager.getMyBankConfiguration(MyBankIntents.DATA_GETCFG_RESPONSE);
        }
    }

    private BroadcastReceiver createBroadcastReceiver() {
        Log.d(TAG, "Create AddReceiptFragment BroadcastReceiver ..");
        return new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Log.d(TAG, "AddReceiptFragment BroadcastReceiver onReceive.. action " + intent.getAction());

                if (intent.getAction().equals(MyBankIntents.DATA_GETCFG_RESPONSE)) {
                    // Do stuff - maybe update my view based on the changed DB contents
                    Log.d(TAG, "Update ElencoBancheSpinner ..");

                    GenericDataContainer gc = (GenericDataContainer) intent.getSerializableExtra("DATI");
                    if (gc != null) {
                        int pos = -1;
                        getAccounts().addAll(gc.getAccounts());
                        getCategories().addAll(gc.getCategories());
                        for (BankAccount ca : gc.getAccounts()) {
                            elencoBancheAdapter.add(ca.getDescription());
                            if (initialAccount != null && initialAccount.equals(ca.getBankName()+"_"+ca.getAccount())) pos = elencoBancheAdapter.getCount()-1;

                        }
                        elencoBancheAdapter.notifyDataSetChanged();
                        for (Category ca : gc.getCategories()) {
                            elencoCategorieAdapter.add(ca.getDescription());
                        }
                        elencoCategorieAdapter.notifyDataSetChanged();
                        if (pos >= 0) elencoBanche.setSelection(pos);
                    }
                }
            }
        };
    }
}
