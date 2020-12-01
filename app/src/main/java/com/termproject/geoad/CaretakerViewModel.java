package com.termproject.geoad;

import androidx.lifecycle.ViewModel;

public class CaretakerViewModel extends ViewModel {
    private Caretaker caretaker;

    public void setCaretaker(Caretaker caretaker) {
        this.caretaker = caretaker;
    }

    public Caretaker getCaretaker() {
        return caretaker;
    }
}
