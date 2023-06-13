package tech.wedev.wecom.controller;

import lombok.SneakyThrows;
import sun.reflect.generics.reflectiveObjects.ParameterizedTypeImpl;
import tech.wedev.wecom.entity.po.BasicPO;
import tech.wedev.wecom.entity.qo.BasicQO;
import tech.wedev.wecom.standard.BasicService;

import javax.annotation.PostConstruct;
import java.lang.reflect.Type;

public abstract class BasicController<P extends BasicPO, Q extends BasicQO> {
    private Class<P> pClass;
    private Class<Q> qClass;

    public Class<P> getpClass() {
        return pClass;
    }

    public Class<Q> getqClass() {
        return qClass;
    }

    @PostConstruct
    @SneakyThrows
    public void init() {
        Type[] actualTypeArguments = ((ParameterizedTypeImpl) this.getClass().getGenericSuperclass()).getActualTypeArguments();
        for (Type actualTypeArgument : actualTypeArguments) {
            String typeName = actualTypeArgument.getTypeName();
            if (typeName.endsWith("QO")) {
                this.qClass = (Class<Q>) Class.forName(typeName);
            } else if (typeName.endsWith("PO")) {
                this.pClass = (Class<P>) Class.forName(typeName);
            }
        }
    }

    public abstract BasicService<P, Q> getBasicService();

}
