package org.appenders.log4j2.elasticsearch;

/*-
 * #%L
 * log4j2-elasticsearch
 * %%
 * Copyright (C) 2019 Rafal Foltynski
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

import org.junit.Test;

import static org.appenders.log4j2.elasticsearch.mock.LifecycleTestHelper.falseOnlyOnce;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class StringAppenderTest {


    @Test
    public void lifecycleStart() {

        // given
        LifeCycle lifeCycle = createLifeCycleTestObject();

        assertTrue(lifeCycle.isStopped());

        // when
        lifeCycle.start();

        // then
        assertFalse(lifeCycle.isStopped());
        assertTrue(lifeCycle.isStarted());

    }

    @Test
    public void lifecycleStop() {

        // given
        LifeCycle lifeCycle = createLifeCycleTestObject();

        assertTrue(lifeCycle.isStopped());

        lifeCycle.start();
        assertTrue(lifeCycle.isStarted());

        // when
        lifeCycle.stop();

        // then
        assertFalse(lifeCycle.isStarted());
        assertTrue(lifeCycle.isStopped());

    }

    @Test
    public void lifecycleStartStartsBatchDelivery() {

        // given
        BatchDelivery batchDelivery = mock(BatchDelivery.class);
        ItemAppender itemAppender = new StringAppender(batchDelivery, logEvent -> null);

        // when
        itemAppender.start();

        // then
        verify(batchDelivery).start();

    }

    @Test
    public void lifecycleStopStopsBatchDeliveryIfStarted() {

        // given
        BatchDelivery batchDelivery = mock(BatchDelivery.class);
        when(batchDelivery.isStarted()).thenReturn(true);

        ItemAppender itemAppender = new StringAppender(batchDelivery, logEvent -> null);

        // when
        itemAppender.stop();

        // then
        verify(batchDelivery).stop();

    }

    @Test
    public void lifecycleStopStopsBatchDeliveryOnlyOnce() {

        // given
        BatchDelivery batchDelivery = mock(BatchDelivery.class);
        when(batchDelivery.isStopped()).thenAnswer(falseOnlyOnce());

        ItemAppender itemAppender = new StringAppender(batchDelivery, logEvent -> null);

        // when
        itemAppender.stop();
        itemAppender.stop();

        // then
        verify(batchDelivery).stop();

    }

    private LifeCycle createLifeCycleTestObject() {
        BatchDelivery batchDelivery = mock(BatchDelivery.class);
        return new StringAppender(batchDelivery, logEvent -> null);
    }

}
