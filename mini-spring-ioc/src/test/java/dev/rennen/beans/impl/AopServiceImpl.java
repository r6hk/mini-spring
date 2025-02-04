package dev.rennen.beans.impl;

import dev.rennen.beans.AopService;

/**
 * <br/> 2025/2/4 22:49
 *
 * @author rennen.dev
 */
public class AopServiceImpl implements AopService {

    @Override
    public boolean doAction(boolean booleanValue) {
        // 若未被代理，返回原值
        // 若被代理，返回代理值（原值取反）
        return booleanValue;
    }
}
