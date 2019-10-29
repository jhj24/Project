package com.zgdj.lib.utils;

import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.text.method.DigitsKeyListener;
import android.widget.TextView;

/**
 * 小数点处理
 * <p>
 * Created by jhj on 18-8-15.
 */

public abstract class DecimalInputTextWatcher implements TextWatcher {

    private TextView editText = null;

    /**
     * 默认小数2位
     */
    private static final int DEFAULT_DECIMAL_DIGITS = 2;
    /**
     * 默认整数9位
     */
    private static final int DEFAULT_INTEGER_DIGITS = 9;

    private int decimalDigits;// 小数的位数
    private int integerDigits;// 整数的位数


    public DecimalInputTextWatcher(TextView editText) {
        this(editText, DEFAULT_DECIMAL_DIGITS);

    }

    /**
     * @param editText      editText
     * @param decimalDigits 小数的位数
     */
    public DecimalInputTextWatcher(TextView editText, int decimalDigits) {
        this(editText, DEFAULT_INTEGER_DIGITS, decimalDigits);
    }

    /**
     * @param editText      editText
     * @param integerDigits 整数的位数
     * @param decimalDigits 小数的位数
     */
    public DecimalInputTextWatcher(TextView editText, int integerDigits, int decimalDigits) {
        this.editText = editText;
        if (integerDigits <= 0)
            throw new RuntimeException("integerDigits must > 0");
        if (decimalDigits <= 0)
            throw new RuntimeException("decimalDigits must > 0");
        this.integerDigits = integerDigits;
        this.decimalDigits = decimalDigits;
        this.editText.setKeyListener(DigitsKeyListener.getInstance("0123456789."));
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void afterTextChanged(Editable editable) {
        String s = editable.toString();
        editText.removeTextChangedListener(this);

        if (s.contains(".")) {

            //设置输入最大长度
            if (integerDigits > 0) {
                editText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(integerDigits + decimalDigits + 1)});
            }

            //小数点只能出现1次
            if (getDisplayCount(s) > 1) {
                s = s.substring(0, s.length() - 1);
                editable.replace(0, editable.length(), s.trim());
            }


            //设置小数点位数
            if (s.length() - (s.indexOf(".") + 1) > decimalDigits) {
                s = s.substring(0, s.length() - 1);
                editable.replace(0, editable.length(), s.trim());
            }


        } else {
            if (integerDigits > 0) {
                editText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(integerDigits + 1)});
                if (s.length() > integerDigits) {
                    s = s.substring(0, integerDigits);
                    editable.replace(0, editable.length(), s.trim());
                }
            }

        }

        //起始为.
        if (s.trim().equals(".")) {
            s = "0" + s;
            editable.replace(0, editable.length(), s.trim());
        }
        //起始为0
        if (s.startsWith("0") && s.trim().length() > 1) {
            if (!s.substring(1, 2).equals(".")) {
                editable.replace(0, editable.length(), "0");
            }
        }

        editText.addTextChangedListener(this);

    }

    /**
     * 计算字符串中小数点出现次数
     *
     * @param s 字符串
     * @return num
     */
    private int getDisplayCount(String s) {
        int num = 0;
        for (int i = 0; i < s.length(); i++) {
            if (s.charAt(i) == '.') {
                num++;
            }
        }
        return num;
    }

}
