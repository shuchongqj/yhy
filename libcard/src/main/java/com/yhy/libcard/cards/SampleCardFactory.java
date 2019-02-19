package com.yhy.libcard.cards;

import com.yhy.libcard.Card;
import com.yhy.libcard.CardFactory;
import com.yhy.libcardcompiler.Factory;

@Factory(type = "sample")
public class SampleCardFactory implements CardFactory{
    @Override
    public Card make() {
        return new SampleCard();
    }
}
