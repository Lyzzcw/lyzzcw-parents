package lyzzcw.work.component.redis.cache.data; /**
 * Copyright 2022-9999 the original author or authors.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


import lyzzcw.work.component.redis.cache.constant.Constant;

import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;

/**
 * @author lzy
 * @version 1.0.0
 * @description 缓存到Redis中的数据，主要配合使用数据的逻辑过期
 */
public class CacheData extends AbstractCache{
    //实际业务数据
    private Object data;
    //过期时间点
    private LocalDateTime expireTime;

    public CacheData() {
    }

    public CacheData(Object data, LocalDateTime expireTime) {
        this.data = data;
        this.expireTime = expireTime;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public LocalDateTime getExpireTime() {
        return expireTime;
    }

    public void setExpireTime(LocalDateTime expireTime) {
        this.expireTime = expireTime;
    }

    public CacheData with(Object data){
        this.data = data;
        this.exist = true;
        return this;
    }

    public CacheData withVersion(Long version){
        this.version = version;
        return this;
    }

    public CacheData retry(){
        this.retryLater = true;
        return this;
    }

    public CacheData empty(){
        this.data = Constant.EMPTY_VALUE;
        this.exist = false;
        return this;
    }

    public CacheData withExpireTime(Long timeout, TimeUnit unit){
        this.expireTime = LocalDateTime.now().plusSeconds(unit.toSeconds(timeout));
        return this;
    }

    @Override
    public String toString() {
        return "CacheData{" +
                "exist=" + exist +
                ", version=" + version +
                ", retryLater=" + retryLater +
                ", data=" + data +
                ", expireTime=" + expireTime +
                '}';
    }
}
