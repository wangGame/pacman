# pacman

取消MapScreen里面的stage，添加相机和batch参数设置。 （stage并没有实际的作用!）

// 设置相机宽高
camera.setToOrtho(false,19,23);

//设置batch让相机和画笔统一
Constant.batch.setProjectionMatrix(camera.combined);


使用两个相机，一个观察地图，小物体，一个观察大物体