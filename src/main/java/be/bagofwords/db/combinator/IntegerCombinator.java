package be.bagofwords.db.combinator;

import be.bagofwords.exec.RemoteClass;

@RemoteClass
public class IntegerCombinator implements Combinator<Integer> {

    @Override
    public Integer combine(Integer first, Integer second) {
        return first + second;
    }
}
