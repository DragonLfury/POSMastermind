package com.lexso.dashboard.swing.table;

import com.lexso.dashboard.model.ModelStudent;

public interface EventAction {

    public void delete(ModelStudent student);

    public void update(ModelStudent student);
}
