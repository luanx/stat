package com.wantdo.stat.web.finance;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * @Date : 2015-9-15
 * @From : stat
 * @Author : luanx@wantdo.com
 */

@Controller
@RequestMapping(value = "/finance/cashflow")
public class CashFlowController {


    @RequestMapping(method = RequestMethod.GET)
    public String list() {
        return "finance/cashflow";
    }
}
