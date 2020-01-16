/*
 * Copyright 2020 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.kie.workbench.common.stunner.bpmn.definition;

import org.junit.Test;
import org.kie.workbench.common.stunner.bpmn.definition.property.background.BackgroundSet;
import org.kie.workbench.common.stunner.bpmn.definition.property.dimensions.RectangleDimensionsSet;
import org.kie.workbench.common.stunner.bpmn.definition.property.font.FontSet;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class BaseArtifactsTest {

    private BaseArtifacts tested = new FakeBaseArtifacts(new BackgroundSet(),
                                                         new FontSet(),
                                                         new RectangleDimensionsSet());

    @Test
    public void getCategory() {
        assertNotNull(tested.getCategory());
    }

    @Test
    public void getBackgroundSet() {
        assertNotNull(tested.getBackgroundSet());
    }

    @Test
    public void getFontSet() {
        assertNotNull(tested.getFontSet());
    }

    @Test
    public void getDimensionsSet() {
        assertNotNull(tested.getDimensionsSet());
    }

    @Test
    public void setBackgroundSet() {
        BackgroundSet backgroundSet = new BackgroundSet();
        tested.setBackgroundSet(backgroundSet);
        assertEquals(backgroundSet, tested.getBackgroundSet());
    }

    @Test
    public void setFontSet() {
        FontSet fontSet = new FontSet();
        tested.setFontSet(fontSet);
        assertEquals(fontSet, tested.getFontSet());
    }

    @Test
    public void setDimensionsSet() {
        RectangleDimensionsSet dimensionsSet = new RectangleDimensionsSet();
        tested.setDimensionsSet(dimensionsSet);
        assertEquals(dimensionsSet, tested.getDimensionsSet());
    }

    @Test
    public void testHashCode() {
        assertNotEquals(new DataObject().hashCode(), tested.hashCode());
        assertNotEquals(new TextAnnotation().hashCode(), tested.hashCode());
    }

    @Test
    public void testEquals() {
        assertTrue(tested.equals(new DataObject()));
    }


    private static class FakeBaseArtifacts extends BaseArtifacts {

        public FakeBaseArtifacts(BackgroundSet backgroundSet, FontSet fontSet, RectangleDimensionsSet dimensionsSet) {
            super(backgroundSet, fontSet, dimensionsSet);
        }

        @Override
        public BPMNBaseInfo getGeneral() {
            return null;
        }
    }
}