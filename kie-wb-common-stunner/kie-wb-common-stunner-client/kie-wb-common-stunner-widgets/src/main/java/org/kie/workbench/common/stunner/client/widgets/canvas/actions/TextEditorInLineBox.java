/*
 * Copyright 2019 Red Hat, Inc. and/or its affiliates.
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

package org.kie.workbench.common.stunner.client.widgets.canvas.actions;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;

import org.kie.workbench.common.stunner.core.client.canvas.controls.actions.InLineTextEditorBox;

@Dependent
@InLineTextEditorBox
public class TextEditorInLineBox extends AbstractTextEditorBox {

    private static final double OFFSET_X = 00.0;

    private static final double OFFSET_Y = -85.0;

    private final TextEditorInLineBoxView view;

    @Inject
    public TextEditorInLineBox(final @InLineTextEditorBox
                                       TextEditorInLineBoxView view) {
        this.view = view;
    }

    @Override
    protected TextEditorBoxView getView() {
        return view;
    }

    @Override
    public double getDisplayOffsetX() {
        return OFFSET_X;
    }

    @Override
    public double getDisplayOffsetY() {
        return OFFSET_Y;
    }

    @Override
    public void setWidth(final double width) {
        view.setWidth(width);
    }

    @Override
    public void setHeight(final double height) {
        view.setHeight(height);
    }

    @Override
    public void setFontSize(double size) {
        view.setFontSize(size);
    }
}
