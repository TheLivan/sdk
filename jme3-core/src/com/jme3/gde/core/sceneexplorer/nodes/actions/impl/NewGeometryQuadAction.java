/*
 *  Copyright (c) 2009-2024 jMonkeyEngine
 *  All rights reserved.
 * 
 *  Redistribution and use in source and binary forms, with or without
 *  modification, are permitted provided that the following conditions are
 *  met:
 * 
 *  * Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 * 
 *  * Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in the
 *    documentation and/or other materials provided with the distribution.
 * 
 *  * Neither the name of 'jMonkeyEngine' nor the names of its contributors
 *    may be used to endorse or promote products derived from this software
 *    without specific prior written permission.
 * 
 *  THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 *  "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED
 *  TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 *  PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 *  CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 *  EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 *  PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 *  PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 *  LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 *  NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 *  SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package com.jme3.gde.core.sceneexplorer.nodes.actions.impl;

import com.jme3.gde.core.sceneexplorer.nodes.actions.AbstractNewSpatialAction;
import com.jme3.gde.core.sceneexplorer.nodes.actions.NewGeometryAction;
import com.jme3.gde.core.sceneexplorer.nodes.primitives.CreateQuadPanel;
import com.jme3.scene.Geometry;
import com.jme3.scene.Mesh;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.CenterQuad;
import com.jme3.scene.shape.Quad;
import org.openide.DialogDescriptor;
import org.openide.DialogDisplayer;
import org.openide.NotifyDescriptor;

/**
 * Action to create a new primitive (Quad)
 *
 * @author MeFisto94
 * @author david.bernard.31
 */
@org.openide.util.lookup.ServiceProvider(service = NewGeometryAction.class)
public class NewGeometryQuadAction extends AbstractNewSpatialAction implements NewGeometryAction {

    CreateQuadPanel form;

    public NewGeometryQuadAction() {
        name = "Quad";
        form = new CreateQuadPanel();
    }

    @Override
    protected Spatial doCreateSpatial(Node parent) {
        Mesh mesh;
        if (form.isCentered()) {
            mesh = new CenterQuad(form.getQuadWidth(), form.getQuadHeight(), form.isFlipCoords());
        } else {
            mesh = new Quad(form.getQuadWidth(), form.getQuadHeight(), form.isFlipCoords());
        }
        
        Geometry geom = form.getNewGeomPanel().handleGeometry(pm, mesh);
        // parent.attachChild(geom); // was present in previous code, but should neither be necessary nor correct
        return geom;
    }

    @Override
    protected boolean prepareCreateSpatial() {
        String msg = "Create new Quad";
        DialogDescriptor dd = new DialogDescriptor(form, msg);
        Object result = DialogDisplayer.getDefault().notify(dd);
        return (result == NotifyDescriptor.OK_OPTION);
    }
}
