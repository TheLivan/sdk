/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * MaterialPreviewWidget.java
 *
 * Created on 1 août 2011, 10:27:05
 */
package com.jme3.gde.materials.multiview.widgets;

import com.jme3.gde.core.assets.ProjectAssetManager;
import com.jme3.gde.core.icons.IconList;
import com.jme3.gde.core.scene.SceneApplication;
import com.jme3.gde.core.scene.SceneRequest;
import com.jme3.gde.materials.MaterialPreviewRenderer;

/**
 *
 * @author Nehon
 */
public class MaterialPreviewWidget extends javax.swing.JPanel {

    private boolean init=false;
    private MaterialPreviewRenderer matRenderer;
    /** Creates new form MaterialPreviewWidget */
    public MaterialPreviewWidget() {
        initComponents();        
    }

    private  void initWidget() {
        sphereButton.setSelected(true);
        matRenderer = new MaterialPreviewRenderer(previewLabel);
        init=true;
    }

    @SuppressWarnings("unchecked")
    public void showMaterial(ProjectAssetManager assetManager, String materialFileName) {
        if(!init){
            initWidget();
        }        
        matRenderer.showMaterial(assetManager, materialFileName);
    }

    public void clear() {
        previewLabel.setIcon(null);
    }

    public void sceneOpened(SceneRequest request) {
    }

    public void sceneClosed(SceneRequest request) {
    }

    public void cleanUp(){
         matRenderer.cleanUp();
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        toggleButtonGroup = new javax.swing.ButtonGroup();
        previewLabel = new javax.swing.JLabel();
        jToolBar1 = new javax.swing.JToolBar();
        sphereButton = new javax.swing.JToggleButton();
        cubeButton = new javax.swing.JToggleButton();
        planeButton = new javax.swing.JToggleButton();
        jSeparator1 = new javax.swing.JToolBar.Separator();
        togglePbrEnvButton = new javax.swing.JToggleButton();

        previewLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        previewLabel.setText(org.openide.util.NbBundle.getMessage(MaterialPreviewWidget.class, "MaterialPreviewWidget.previewLabel.text")); // NOI18N
        previewLabel.setMaximumSize(new java.awt.Dimension(120, 120));
        previewLabel.setMinimumSize(new java.awt.Dimension(120, 120));

        jToolBar1.setRollover(true);

        toggleButtonGroup.add(sphereButton);
        sphereButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/jme3/gde/materials/multiview/widgets/icons/sphere.png"))); // NOI18N
        sphereButton.setSelected(true);
        sphereButton.setText(org.openide.util.NbBundle.getMessage(MaterialPreviewWidget.class, "MaterialPreviewWidget.sphereButton.text")); // NOI18N
        sphereButton.setToolTipText(org.openide.util.NbBundle.getMessage(MaterialPreviewWidget.class, "MaterialPreviewWidget.sphereButton.toolTipText")); // NOI18N
        sphereButton.setFocusable(false);
        sphereButton.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        sphereButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        sphereButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        sphereButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                sphereButtonActionPerformed(evt);
            }
        });
        jToolBar1.add(sphereButton);

        toggleButtonGroup.add(cubeButton);
        cubeButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/jme3/gde/materials/multiview/widgets/icons/box.png"))); // NOI18N
        cubeButton.setText(org.openide.util.NbBundle.getMessage(MaterialPreviewWidget.class, "MaterialPreviewWidget.cubeButton.text")); // NOI18N
        cubeButton.setToolTipText(org.openide.util.NbBundle.getMessage(MaterialPreviewWidget.class, "MaterialPreviewWidget.cubeButton.toolTipText")); // NOI18N
        cubeButton.setFocusable(false);
        cubeButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        cubeButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        cubeButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cubeButtonActionPerformed(evt);
            }
        });
        jToolBar1.add(cubeButton);

        toggleButtonGroup.add(planeButton);
        planeButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/jme3/gde/materials/multiview/widgets/icons/plane.png"))); // NOI18N
        planeButton.setText(org.openide.util.NbBundle.getMessage(MaterialPreviewWidget.class, "MaterialPreviewWidget.planeButton.text")); // NOI18N
        planeButton.setToolTipText(org.openide.util.NbBundle.getMessage(MaterialPreviewWidget.class, "MaterialPreviewWidget.planeButton.toolTipText")); // NOI18N
        planeButton.setFocusable(false);
        planeButton.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        planeButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        planeButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        planeButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                planeButtonActionPerformed(evt);
            }
        });
        jToolBar1.add(planeButton);
        jToolBar1.add(jSeparator1);

        togglePbrEnvButton.setIcon(IconList.lightYellow);
        togglePbrEnvButton.setToolTipText(org.openide.util.NbBundle.getMessage(MaterialPreviewWidget.class, "MaterialPreviewWidget.togglePbrEnvButton.toolTipText")); // NOI18N
        togglePbrEnvButton.setFocusable(false);
        togglePbrEnvButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        togglePbrEnvButton.setLabel(org.openide.util.NbBundle.getMessage(MaterialPreviewWidget.class, "MaterialPreviewWidget.togglePbrEnvButton.label")); // NOI18N
        togglePbrEnvButton.setMaximumSize(new java.awt.Dimension(26, 24));
        togglePbrEnvButton.setMinimumSize(new java.awt.Dimension(26, 24));
        togglePbrEnvButton.setName("togglePbrEnvButton"); // NOI18N
        togglePbrEnvButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        togglePbrEnvButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                togglePbrEnvButtonActionPerformed(evt);
            }
        });
        jToolBar1.add(togglePbrEnvButton);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(previewLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 180, Short.MAX_VALUE)
            .addComponent(jToolBar1, javax.swing.GroupLayout.DEFAULT_SIZE, 180, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addComponent(previewLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 120, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jToolBar1, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
    }// </editor-fold>//GEN-END:initComponents

private void sphereButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_sphereButtonActionPerformed
    matRenderer.switchDisplay(MaterialPreviewRenderer.DisplayType.Sphere);
}//GEN-LAST:event_sphereButtonActionPerformed

private void cubeButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cubeButtonActionPerformed
    matRenderer.switchDisplay(MaterialPreviewRenderer.DisplayType.Box);
}//GEN-LAST:event_cubeButtonActionPerformed

private void planeButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_planeButtonActionPerformed
    matRenderer.switchDisplay(MaterialPreviewRenderer.DisplayType.Quad);
}//GEN-LAST:event_planeButtonActionPerformed

    private void togglePbrEnvButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_togglePbrEnvButtonActionPerformed
        SceneApplication.getApplication().enablePreviewLighting(togglePbrEnvButton.isSelected());
        matRenderer.refreshOnly();
    }//GEN-LAST:event_togglePbrEnvButtonActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JToggleButton cubeButton;
    private javax.swing.JToolBar.Separator jSeparator1;
    private javax.swing.JToolBar jToolBar1;
    private javax.swing.JToggleButton planeButton;
    private javax.swing.JLabel previewLabel;
    private javax.swing.JToggleButton sphereButton;
    private javax.swing.ButtonGroup toggleButtonGroup;
    private javax.swing.JToggleButton togglePbrEnvButton;
    // End of variables declaration//GEN-END:variables
}
