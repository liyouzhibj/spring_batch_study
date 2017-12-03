package com.youzhi.batch.sample.ch02.processor;

import com.youzhi.batch.sample.ch02.model.CreditBill;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Service;

/**
 * @description 信用卡业务处理
 * @create by youzhi.li on 2017-12-02 10:32
 **/
public class CreditBillItemProcessor implements ItemProcessor<CreditBill, CreditBill>{

    @Override
    public CreditBill process(CreditBill creditBill) throws Exception {
        System.out.println(creditBill.toString());
        return creditBill;
    }
}
