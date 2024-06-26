/*
 * Copyright (c) 2009-2010 jMonkeyEngine All rights reserved. <p/>
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * 
 * * Redistributions of source code must retain the above copyright notice,
 * this list of conditions and the following disclaimer. <p/> * Redistributions
 * in binary form must reproduce the above copyright notice, this list of
 * conditions and the following disclaimer in the documentation and/or other
 * materials provided with the distribution. <p/> * Neither the name of
 * 'jMonkeyEngine' nor the names of its contributors may be used to endorse or
 * promote products derived from this software without specific prior written
 * permission. <p/> THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND
 * CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT
 * NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A
 * PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS;
 * OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY,
 * WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR
 * OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF
 * ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package com.jme3.gde.core.sceneviewer;

import com.jme3.asset.AssetManager;
import com.jme3.asset.MaterialKey;
import com.jme3.collision.CollisionResult;
import com.jme3.collision.CollisionResults;
import com.jme3.gde.core.dnd.SceneViewerDropTargetListener;
import com.jme3.gde.core.filters.FilterExplorerTopComponent;
import com.jme3.gde.core.icons.IconList;
import com.jme3.gde.core.scene.SceneApplication;
import com.jme3.gde.core.scene.SceneRequest;
import com.jme3.input.awt.AwtKeyInput;
import com.jme3.input.event.KeyInputEvent;
import com.jme3.material.Material;
import com.jme3.math.Ray;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import java.awt.Component;
import java.awt.dnd.DropTarget;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.util.concurrent.Callable;
import java.util.logging.Logger;
import org.netbeans.api.settings.ConvertAsProperties;
import org.openide.awt.UndoRedo;
import org.openide.util.HelpCtx;
import org.openide.util.ImageUtilities;
import org.openide.util.Lookup;
import org.openide.util.NbBundle;
import org.openide.windows.TopComponent;
import org.openide.windows.WindowManager;

/**
 * This is the component containing the whole scene/model<br />
 * It also contains the top bar.
 */
@ConvertAsProperties(dtd = "-//com.jme3.gde.core.sceneviewer//SceneViewer//EN",
        autostore = false)
public final class SceneViewerTopComponent extends TopComponent {

    private static SceneViewerTopComponent instance;
    /**
     * path to the icon used by the component and its open action
     */
    static final String ICON_PATH = IconList.JME_LOGO;
    private static final String PREFERRED_ID = "SceneViewerTopComponent";
    private SceneApplication app;
    private HelpCtx helpContext = new HelpCtx("com.jme3.gde.core.sceneviewer");
    private Component oglCanvas;

    public SceneViewerTopComponent() {
        initComponents();
        oGLPanel.setMinimumSize(new java.awt.Dimension(10, 10));
        setFocusable(true);
        setName(NbBundle.getMessage(SceneViewerTopComponent.class, "CTL_SceneViewerTopComponent"));
        setToolTipText(NbBundle.getMessage(SceneViewerTopComponent.class, "HINT_SceneViewerTopComponent"));
        setIcon(ImageUtilities.loadImage(ICON_PATH, true));
        try {
            app = SceneApplication.getApplication();
            oglCanvas = app.getMainPanel();
            oglCanvas.setFocusable(false);
            oGLPanel.add(oglCanvas);

        } catch (Exception e) {
            SceneApplication.showStartupErrorMessage(e);
        } catch (Error err) {
            SceneApplication.showStartupErrorMessage(err);
        }
        //TODO: camera tools (see SwitchFrontViewAction)
//        Collection<? extends Action> result = Lookups.forPath("CameraTools").lookupAll(Action.class);
//        for (Iterator<? extends Action> it = result.iterator(); it.hasNext();) {
//            Action object = it.next();
//            //System.out.println("lookup object! / " + object);
//        }

        //We add a mouse wheel listener to the top conmponent in order to correctly dispatch the event ot the cam controller
        //the oGLPanel may naver have the focus.
        //  if ("true".equals(NbPreferences.forModule(Installer.class).get("use_lwjgl_canvas", "false"))) {
        addMouseWheelListener(new MouseWheelListener() {
            @Override
            public void mouseWheelMoved(final MouseWheelEvent e) {
                SceneApplication.getApplication().enqueue(new Callable<Void>() {
                    @Override
                    public Void call() throws Exception {
                        String action;
                        if (e.getWheelRotation() < 0) {
                            action = "MouseWheel";
                        } else if (e.getWheelRotation() > 0) {
                            action = "MouseWheel-";
                        } else {
                            return null;
                        }
                        if (app.getActiveCameraController() != null) {
                            app.getActiveCameraController().onAnalog(action, e.getWheelRotation(), 0);
                        }
                        return null;
                    }
                });
            }
        });
        addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent evt) {
            }

            @Override
            public void keyPressed(final KeyEvent evt) {
                SceneApplication.getApplication().enqueue(new Callable<Void>() {
                    @Override
                    public Void call() throws Exception {
                        int code = AwtKeyInput.convertAwtKey(evt.getKeyCode());
                        KeyInputEvent keyEvent = new KeyInputEvent(code, evt.getKeyChar(), true, false);
                        keyEvent.setTime(evt.getWhen());
                        if (app.getActiveCameraController() != null) {
                            app.getActiveCameraController().onKeyEvent(keyEvent);
                        }
                        return null;
                    }
                });
            }

            @Override
            public void keyReleased(final KeyEvent evt) {
                SceneApplication.getApplication().enqueue(new Callable<Void>() {
                    @Override
                    public Void call() throws Exception {
                        int code = AwtKeyInput.convertAwtKey(evt.getKeyCode());
                        KeyInputEvent keyEvent = new KeyInputEvent(code, evt.getKeyChar(), false, false);
                        keyEvent.setTime(evt.getWhen());
                        if (app.getActiveCameraController() != null) {
                            app.getActiveCameraController().onKeyEvent(keyEvent);
                        }
                        return null;
                    }
                });
            }
        });
        //}

        oGLPanel.setDropTarget(new DropTarget(this, new SceneViewerDropTargetListener(this)));
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jToolBar1 = new javax.swing.JToolBar();
        enableCamLight = new javax.swing.JToggleButton();
        jToggleButton1 = new javax.swing.JToggleButton();
        jSeparator1 = new javax.swing.JToolBar.Separator();
        enableWireframe = new javax.swing.JToggleButton();
        enablePBREnv = new javax.swing.JToggleButton();
        enablePBRSky = new javax.swing.JToggleButton();
        enableNormalView = new javax.swing.JToggleButton();
        jPanel1 = new javax.swing.JPanel();
        enableStats = new javax.swing.JToggleButton();
        oGLPanel = new javax.swing.JPanel();

        setLayout(new java.awt.BorderLayout());

        jToolBar1.setRollover(true);
        jToolBar1.setMinimumSize(new java.awt.Dimension(110, 40));
        jToolBar1.setPreferredSize(new java.awt.Dimension(110, 40));

        enableCamLight.setIcon(IconList.lightOff);
        org.openide.awt.Mnemonics.setLocalizedText(enableCamLight, org.openide.util.NbBundle.getMessage(SceneViewerTopComponent.class, "SceneViewerTopComponent.enableCamLight.text")); // NOI18N
        enableCamLight.setToolTipText(org.openide.util.NbBundle.getMessage(SceneViewerTopComponent.class, "SceneViewerTopComponent.enableCamLight.toolTipText")); // NOI18N
        enableCamLight.setFocusable(false);
        enableCamLight.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        enableCamLight.setMaximumSize(new java.awt.Dimension(32, 32));
        enableCamLight.setMinimumSize(new java.awt.Dimension(32, 32));
        enableCamLight.setPreferredSize(new java.awt.Dimension(32, 32));
        enableCamLight.setSelectedIcon(IconList.lightOn);
        enableCamLight.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        enableCamLight.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                enableCamLightActionPerformed(evt);
            }
        });
        jToolBar1.add(enableCamLight);

        jToggleButton1.setIcon(IconList.eyeOff);
        org.openide.awt.Mnemonics.setLocalizedText(jToggleButton1, org.openide.util.NbBundle.getMessage(SceneViewerTopComponent.class, "SceneViewerTopComponent.jToggleButton1.text")); // NOI18N
        jToggleButton1.setToolTipText(org.openide.util.NbBundle.getMessage(SceneViewerTopComponent.class, "SceneViewerTopComponent.jToggleButton1.toolTipText")); // NOI18N
        jToggleButton1.setFocusable(false);
        jToggleButton1.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jToggleButton1.setMaximumSize(new java.awt.Dimension(32, 32));
        jToggleButton1.setMinimumSize(new java.awt.Dimension(32, 32));
        jToggleButton1.setPreferredSize(new java.awt.Dimension(32, 32));
        jToggleButton1.setSelectedIcon(IconList.eyeGreen);
        jToggleButton1.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jToggleButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jToggleButton1ActionPerformed(evt);
            }
        });
        jToolBar1.add(jToggleButton1);

        jSeparator1.setPreferredSize(new java.awt.Dimension(32, 32));
        jToolBar1.add(jSeparator1);

        enableWireframe.setIcon(IconList.colorBox);
        org.openide.awt.Mnemonics.setLocalizedText(enableWireframe, org.openide.util.NbBundle.getMessage(SceneViewerTopComponent.class, "SceneViewerTopComponent.enableWireframe.text")); // NOI18N
        enableWireframe.setToolTipText(org.openide.util.NbBundle.getMessage(SceneViewerTopComponent.class, "SceneViewerTopComponent.enableWireframe.toolTipText")); // NOI18N
        enableWireframe.setFocusable(false);
        enableWireframe.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        enableWireframe.setMaximumSize(new java.awt.Dimension(32, 32));
        enableWireframe.setMinimumSize(new java.awt.Dimension(32, 32));
        enableWireframe.setPreferredSize(new java.awt.Dimension(32, 32));
        enableWireframe.setSelectedIcon(IconList.wireBox);
        enableWireframe.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        enableWireframe.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                enableWireframeActionPerformed(evt);
            }
        });
        jToolBar1.add(enableWireframe);

        enablePBREnv.setIcon(IconList.enablePbrEnvironment);
        org.openide.awt.Mnemonics.setLocalizedText(enablePBREnv, org.openide.util.NbBundle.getMessage(SceneViewerTopComponent.class, "SceneViewerTopComponent.enablePBREnv.text")); // NOI18N
        enablePBREnv.setToolTipText(org.openide.util.NbBundle.getMessage(SceneViewerTopComponent.class, "SceneViewerTopComponent.enablePBREnv.toolTipText")); // NOI18N
        enablePBREnv.setFocusable(false);
        enablePBREnv.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        enablePBREnv.setMaximumSize(new java.awt.Dimension(32, 32));
        enablePBREnv.setMinimumSize(new java.awt.Dimension(32, 32));
        enablePBREnv.setPreferredSize(new java.awt.Dimension(32, 32));
        enablePBREnv.setSelectedIcon(IconList.enablePbrEnvironment);
        enablePBREnv.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        enablePBREnv.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                enablePBREnvActionPerformed(evt);
            }
        });
        jToolBar1.add(enablePBREnv);

        enablePBRSky.setIcon(IconList.enablePbrSky);
        org.openide.awt.Mnemonics.setLocalizedText(enablePBRSky, org.openide.util.NbBundle.getMessage(SceneViewerTopComponent.class, "SceneViewerTopComponent.enablePBRSky.text")); // NOI18N
        enablePBRSky.setToolTipText(org.openide.util.NbBundle.getMessage(SceneViewerTopComponent.class, "SceneViewerTopComponent.enablePBRSky.toolTipText")); // NOI18N
        enablePBRSky.setFocusable(false);
        enablePBRSky.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        enablePBRSky.setMaximumSize(new java.awt.Dimension(32, 32));
        enablePBRSky.setMinimumSize(new java.awt.Dimension(32, 32));
        enablePBRSky.setPreferredSize(new java.awt.Dimension(32, 32));
        enablePBRSky.setSelectedIcon(IconList.enablePbrSky);
        enablePBRSky.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        enablePBRSky.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                enablePBRSkyActionPerformed(evt);
            }
        });
        jToolBar1.add(enablePBRSky);

        enableNormalView.setIcon(IconList.normalView);
        org.openide.awt.Mnemonics.setLocalizedText(enableNormalView, org.openide.util.NbBundle.getMessage(SceneViewerTopComponent.class, "SceneViewerTopComponent.enableNormalView.text")); // NOI18N
        enableNormalView.setToolTipText(org.openide.util.NbBundle.getMessage(SceneViewerTopComponent.class, "SceneViewerTopComponent.enableNormalView.toolTipText")); // NOI18N
        enableNormalView.setFocusable(false);
        enableNormalView.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        enableNormalView.setSelectedIcon(IconList.normalView);
        enableNormalView.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        enableNormalView.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                enableNormalViewActionPerformed(evt);
            }
        });
        jToolBar1.add(enableNormalView);
        jToolBar1.add(jPanel1);

        enableStats.setIcon(IconList.sceneInfo);
        org.openide.awt.Mnemonics.setLocalizedText(enableStats, org.openide.util.NbBundle.getMessage(SceneViewerTopComponent.class, "SceneViewerTopComponent.enableStats.text")); // NOI18N
        enableStats.setToolTipText(org.openide.util.NbBundle.getMessage(SceneViewerTopComponent.class, "SceneViewerTopComponent.enableStats.toolTipText")); // NOI18N
        enableStats.setFocusable(false);
        enableStats.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        enableStats.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        enableStats.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                enableStatsActionPerformed(evt);
            }
        });
        jToolBar1.add(enableStats);

        add(jToolBar1, java.awt.BorderLayout.NORTH);

        oGLPanel.setPreferredSize(new java.awt.Dimension(100, 100));
        oGLPanel.setLayout(new java.awt.GridLayout(1, 0));
        add(oGLPanel, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents

    private void enableCamLightActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_enableCamLightActionPerformed
        app.enableCamLight(enableCamLight.isSelected());
    }//GEN-LAST:event_enableCamLightActionPerformed

    private void enableWireframeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_enableWireframeActionPerformed
        app.enableWireFrame(enableWireframe.isSelected());
    }//GEN-LAST:event_enableWireframeActionPerformed

    private void enableStatsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_enableStatsActionPerformed
        app.enableStats(enableStats.isSelected());
    }//GEN-LAST:event_enableStatsActionPerformed

    private void jToggleButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jToggleButton1ActionPerformed
        FilterExplorerTopComponent.findInstance().setFilterEnabled(jToggleButton1.isSelected());
    }//GEN-LAST:event_jToggleButton1ActionPerformed

    private void enablePBREnvActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_enablePBREnvActionPerformed
        app.enablePBRProbe(enablePBREnv.isSelected());
    }//GEN-LAST:event_enablePBREnvActionPerformed

    private void enablePBRSkyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_enablePBRSkyActionPerformed
        app.enablePBRSkybox(enablePBRSky.isSelected());
    }//GEN-LAST:event_enablePBRSkyActionPerformed

    private void enableNormalViewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_enableNormalViewActionPerformed
        app.enableNormalView(enableNormalView.isSelected());
    }//GEN-LAST:event_enableNormalViewActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JToggleButton enableCamLight;
    private javax.swing.JToggleButton enableNormalView;
    private javax.swing.JToggleButton enablePBREnv;
    private javax.swing.JToggleButton enablePBRSky;
    private javax.swing.JToggleButton enableStats;
    private javax.swing.JToggleButton enableWireframe;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JToolBar.Separator jSeparator1;
    private javax.swing.JToggleButton jToggleButton1;
    private javax.swing.JToolBar jToolBar1;
    private javax.swing.JPanel oGLPanel;
    // End of variables declaration//GEN-END:variables

    /**
     * Gets default instance. Do not use directly: reserved for *.settings files
     * only, i.e. deserialization routines; otherwise you could get a
     * non-deserialized instance. To obtain the singleton instance, use
     * {@link #findInstance}.
     *
     * @return
     */
    public static synchronized SceneViewerTopComponent getDefault() {
        if (instance == null) {
            instance = new SceneViewerTopComponent();
        }
        return instance;
    }

    /**
     * Obtain the SceneViewerTopComponent instance. Never call
     * {@link #getDefault} directly!
     *
     * @return
     */
    public static synchronized SceneViewerTopComponent findInstance() {
        TopComponent win = WindowManager.getDefault().findTopComponent(PREFERRED_ID);
        if (win == null) {
            Logger.getLogger(SceneViewerTopComponent.class.getName()).warning(
                    "Cannot find " + PREFERRED_ID + " component. It will not be located properly in the window system.");
            return getDefault();
        }
        if (win instanceof SceneViewerTopComponent) {
            return (SceneViewerTopComponent) win;
        }
        Logger.getLogger(SceneViewerTopComponent.class.getName()).warning(
                "There seem to be multiple components with the '" + PREFERRED_ID
                + "' ID. That is a potential source of errors and unexpected behavior.");
        return getDefault();
    }

    @Override
    public int getPersistenceType() {
        return TopComponent.PERSISTENCE_NEVER;
    }

    @Override
    public HelpCtx getHelpCtx() {
        //this call is for single components:
        //HelpCtx.setHelpIDString(this, "com.jme3.gde.core.sceneviewer");
        return helpContext;
    }

    public void setHelpContext(HelpCtx ctx) {
        this.helpContext = ctx;
    }

    @Override
    public void componentOpened() {
        super.componentOpened();
    }

    @Override
    protected void componentShowing() {
        super.componentShowing();
    }

    @Override
    protected void componentHidden() {
        super.componentHidden();
    }

    @Override
    public void componentClosed() {
        super.componentClosed();
//        oglCanvas.setActiveUpdates(false);
        SceneRequest req = SceneApplication.getApplication().getCurrentSceneRequest();
        if (req != null) {
            enableCamLight.setSelected(false);
            enableStats.setSelected(false);
            SceneApplication.getApplication().closeScene(req);
        }
    }

    void writeProperties(java.util.Properties p) {
        // better to version settings since initial version as advocated at
        // http://wiki.apidesign.org/wiki/PropertyFiles
        p.setProperty("version", "1.0");
        // TODO store your settings
    }

    Object readProperties(java.util.Properties p) {
        if (instance == null) {
            instance = this;
        }
        instance.readPropertiesImpl(p);
        return instance;
    }

    private void readPropertiesImpl(java.util.Properties p) {
        String version = p.getProperty("version");
        // TODO read your settings according to their version
    }

    @Override
    protected String preferredID() {
        return PREFERRED_ID;
    }

    @Override
    public UndoRedo getUndoRedo() {
        return Lookup.getDefault().lookup(UndoRedo.class);
    }

    public void applyMaterial(String assetName, Vector2f cursorPosition) {
        AssetManager assetManager = app.getAssetManager();
        Spatial spatial = pickWorldSpatial(app.getCamera(), new Vector2f(cursorPosition.x, app.getCamera().getHeight() - cursorPosition.y), app.getRootNode());
        System.out.println("position " + new Vector2f(cursorPosition.x, app.getCamera().getHeight() - cursorPosition.y));
        if (spatial != null) {
            Material material = assetManager.loadAsset(new MaterialKey(assetName));
            spatial.setMaterial(material);
        }
    }

    public void addModel(String assetName, Vector2f cursorPosition) {
        AssetManager assetManager = app.getAssetManager();
        Spatial spatial = assetManager.loadModel(assetName);
        CollisionResult cr = pick(app.getCamera(), cursorPosition, app.getRootNode());
        spatial.setLocalTranslation(cr != null ? 
                cr.getContactPoint() : app.getCamera().getWorldCoordinates(cursorPosition, 100f));
        app.getRootNode().attachChild(spatial);
    }

    public static Spatial pickWorldSpatial(Camera cam, Vector2f mouseLoc, Node jmeRootNode) {
        CollisionResult cr = pick(cam, mouseLoc, jmeRootNode);
        if (cr != null) {
            return cr.getGeometry();
        } else {
            return null;
        }
    }

    private static CollisionResult pick(Camera cam, Vector2f mouseLoc, Node node) {
        CollisionResults results = new CollisionResults();
        Ray ray = new Ray();
        Vector3f pos = cam.getWorldCoordinates(mouseLoc, 0).clone();
        Vector3f dir = cam.getWorldCoordinates(mouseLoc, 0.125f).clone();
        dir.subtractLocal(pos).normalizeLocal();
        ray.setOrigin(pos);
        ray.setDirection(dir);
        node.collideWith(ray, results);
        CollisionResult result = results.getClosestCollision();
        return result;
    }
}
