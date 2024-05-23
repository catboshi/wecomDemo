package tech.wedev.wecom.entity.bo;

import lombok.Data;

@Data
public class Int {
    private int value;
    public Int(int value){
        this.value = value;
    }
    public void incr() {
        value++;
    }
}
