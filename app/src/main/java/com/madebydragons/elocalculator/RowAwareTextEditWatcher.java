package com.madebydragons.elocalculator;

import android.text.TextWatcher;
import android.widget.TableRow;

/**
 * Created by fabioa on 3/4/16.
 */
public abstract class RowAwareTextEditWatcher implements TextWatcher {
    private TableRow m_tr = null;
    public void setTableRow(TableRow tr){ m_tr = tr;}
    public TableRow getTableRow() { return m_tr; }
    public RowAwareTextEditWatcher(TableRow tableRow) { m_tr = tableRow;}
}
