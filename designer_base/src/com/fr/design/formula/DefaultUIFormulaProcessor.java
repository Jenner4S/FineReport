package com.fr.design.formula;

import com.fr.design.fun.UIFormulaProcessor;

/**
 * @author richie
 * @date 2015-04-17
 * @since 8.0
 */
public class DefaultUIFormulaProcessor implements UIFormulaProcessor {
    @Override
    public UIFormula appearanceFormula() {
        return new FormulaPane();
    }

    @Override
    public UIFormula appearanceWhenReserveFormula() {
        return new FormulaPaneWhenReserveFormula();
    }
}