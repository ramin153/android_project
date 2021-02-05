package com.example.androidproject.recyclerview;

public enum TypeOfNote
{
    REMINDER(0) ,
    EMAIL(1),
    ACTION(2);

    private int val;

    TypeOfNote(int numVal) {
        val = numVal;
    }
    public static int reminder()
    {
        return REMINDER.getVal();
    }
    public static int action()
    {
        return ACTION.getVal();
    }
    public static int email()
    {
        return EMAIL.getVal();
    }
    public static TypeOfNote convertInt(int type)
    {
        if (type == reminder())
            return TypeOfNote.REMINDER;
        else if (type == action())
            return TypeOfNote.ACTION;
        else
            return TypeOfNote.EMAIL;

    }
    public int getVal() {
        return val;
    }
}
