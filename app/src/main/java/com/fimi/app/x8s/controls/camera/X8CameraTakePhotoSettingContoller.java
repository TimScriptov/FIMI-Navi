package com.fimi.app.x8s.controls.camera;

import android.content.Context;
import android.view.View;
import android.view.ViewStub;

import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.fimi.android.app.R;
import com.fimi.app.x8s.entity.PhotoSubParamItemEntity;
import com.fimi.app.x8s.interfaces.AbsX8Controllers;
import com.fimi.app.x8s.interfaces.IX8CameraMainSetListener;
import com.fimi.app.x8s.viewHolder.CameraParamListener;
import com.fimi.host.HostLogBack;
import com.fimi.kernel.dataparser.usb.JsonUiCallBackListener;
import com.fimi.x8sdk.command.CameraJsonCollection;
import com.fimi.x8sdk.controller.CameraManager;
import com.fimi.x8sdk.entity.X8CameraParamsValue;
import com.fimi.x8sdk.jsonResult.CameraParamsJson;
import com.fimi.x8sdk.jsonResult.CurParamsJson;
import com.fimi.x8sdk.modulestate.StateManager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


public class X8CameraTakePhotoSettingContoller extends AbsX8Controllers implements CameraParamListener, JsonUiCallBackListener {
    PhotoSubParamItemEntity itemEntity;
    Map<String, String> paramMap;
    private CameraManager cameraManager;
    private Context context;
    private IX8CameraMainSetListener mainSetListener;
    private X8CameraParamsController paramController;
    private View paramView;
    private ViewStub paramViewStub;
    private X8CameraSubParamsController subParamsController;
    private View subView;
    private ViewStub subViewStub;
    private boolean tokenEnable;

    public X8CameraTakePhotoSettingContoller(View rootView) {
        super(rootView);
        this.itemEntity = new PhotoSubParamItemEntity();
        this.paramMap = new HashMap();
        this.tokenEnable = true;
    }

    public void setCameraManager(CameraManager cameraManager) {
        if (cameraManager != null) {
            this.cameraManager = cameraManager;
        }
    }

    public void setCurModel() {
        if (this.paramController != null) {
            this.paramController.updateMode(CameraParamStatus.modelStatus, this.paramMap);
            if (this.paramMap != null) {
                for (Map.Entry<String, String> entry : this.paramMap.entrySet()) {
                }
            }
        }
        this.paramController.openUi();
        if (this.subParamsController != null) {
            this.subParamsController.closeUi();
        }
    }

    @Override
    public void initViews(View rootView) {
        this.context = rootView.getContext();
        this.handleView = rootView.findViewById(R.id.x8_mode_setting_layout);
        this.paramViewStub = rootView.findViewById(R.id.param_default_layout);
        this.subViewStub = rootView.findViewById(R.id.sub_param_layout);
        if (this.paramView == null) {
            this.paramView = this.paramViewStub.inflate();
        }
        this.paramController = new X8CameraParamsController(rootView);
        this.paramController.setParamListener(this);
    }

    @Override
    public void initActions() {
    }

    @Override
    public void defaultVal() {
    }

    @Override
    public void gotoSubItem(String key, String value, RecyclerView.ViewHolder viewHolder) {
        if (this.subView == null) {
            this.subView = this.subViewStub.inflate();
            this.subParamsController = new X8CameraSubParamsController(this.rootView);
            this.subParamsController.setCameraManager(this.cameraManager);
            this.subParamsController.setParamListener(this);
            this.subParamsController.setMainSetListener(this.mainSetListener);
        }
        if (this.itemEntity.getOptions() != null && this.itemEntity.getOptions().size() > 0) {
            this.itemEntity.getOptions().clear();
            this.itemEntity.getOptionMap().clear();
            this.itemEntity.setParamKey("");
            CameraJsonCollection.isClearData = true;
        }
        if (this.cameraManager != null && this.tokenEnable) {
            if (key.equals(CameraJsonCollection.KEY_RECORD_MODE)) {
                String[] recordArray = this.context.getResources().getStringArray(R.array.x8_timelapse_record_array);
                this.itemEntity.setOptions(new ArrayList());
                for (String mode : recordArray) {
                    this.itemEntity.getOptions().add(mode);
                }
                this.itemEntity.getOptions().add(0, this.context.getResources().getString(R.string.x8_record_mode));
                matchPhotoMode(this.itemEntity.getOptions(), this.itemEntity.getOptionMap());
                this.itemEntity.setParamKey(CameraJsonCollection.KEY_RECORD_MODE);
                this.itemEntity.setParamValue(this.paramMap.get(CameraJsonCollection.KEY_RECORD_MODE));
                this.itemEntity.setTitleName(this.context.getResources().getString(R.string.x8_record_mode));
                this.subParamsController.setSubParam(this.itemEntity);
            } else if (key.equals("capture_mode")) {
                String[] timelapseCaptureArray = this.context.getResources().getStringArray(R.array.x8_timelapse_capture_array);
                this.itemEntity.setOptions(new ArrayList());
                for (String timelapseCapture : timelapseCaptureArray) {
                    this.itemEntity.getOptions().add(timelapseCapture);
                }
                this.itemEntity.getOptions().add(0, this.context.getResources().getString(R.string.x8_photo_mode));
                matchPhotoMode(this.itemEntity.getOptions(), this.itemEntity.getOptionMap());
                this.itemEntity.setParamKey("capture_mode");
                this.itemEntity.setParamValue(this.paramMap.get("capture_mode"));
                this.itemEntity.setTitleName(this.context.getResources().getString(R.string.x8_photo_mode));
                this.subParamsController.setSubParam(this.itemEntity);
            } else if (key.equals("photo_size")) {
                this.itemEntity.setTitleName(this.context.getResources().getString(R.string.x8_photo_size));
                String[] sizeArray = this.context.getResources().getStringArray(R.array.x8_photo_size_array);
                this.itemEntity.setParamKey("photo_size");
                this.itemEntity.setOptions(new ArrayList());
                for (String size : sizeArray) {
                    this.itemEntity.getOptions().add(size);
                }
                this.itemEntity.getOptions().add(0, this.context.getResources().getString(R.string.x8_photo_size));
                this.itemEntity.setParamValue(this.paramMap.get("photo_size"));
                this.subParamsController.setSubParam(this.itemEntity);
                this.cameraManager.getCameraKeyOptions("photo_size", this);
            } else if (key.equals("photo_format")) {
                this.itemEntity.setTitleName(this.context.getResources().getString(R.string.x8_photo_format));
                String[] sizeArray2 = this.context.getResources().getStringArray(R.array.x8_photo_format_array);
                this.itemEntity.setParamKey("photo_format");
                this.itemEntity.setOptions(new ArrayList());
                for (String size2 : sizeArray2) {
                    this.itemEntity.getOptions().add(size2);
                }
                this.itemEntity.getOptions().add(0, this.context.getResources().getString(R.string.x8_photo_format));
                this.itemEntity.setParamValue(this.paramMap.get("photo_format"));
                this.subParamsController.setSubParam(this.itemEntity);
                this.cameraManager.getCameraKeyOptions("photo_format", this);
            } else if (key.equals("awb")) {
                this.itemEntity.setTitleName(this.context.getResources().getString(R.string.x8_camera_awb));
                String[] sizeArray3 = this.context.getResources().getStringArray(R.array.x8_awb_array);
                this.itemEntity.setParamKey("awb");
                this.itemEntity.setOptions(new ArrayList());
                for (String size3 : sizeArray3) {
                    this.itemEntity.getOptions().add(size3);
                }
                this.itemEntity.getOptions().add(0, this.context.getResources().getString(R.string.x8_camera_awb));
                this.itemEntity.setParamValue(this.paramMap.get("awb"));
                this.subParamsController.setSubParam(this.itemEntity);
                this.cameraManager.getCameraKeyOptions("awb", this);
            } else if (key.equals(CameraJsonCollection.KEY_METERMING_MODE)) {
                this.cameraManager.getCameraKeyOptions(CameraJsonCollection.KEY_METERMING_MODE, this);
                this.itemEntity.setParamValue(this.paramMap.get(CameraJsonCollection.KEY_METERMING_MODE));
                this.itemEntity.setTitleName(this.context.getResources().getString(R.string.x8_camera_metering));
            } else if (key.equals(CameraJsonCollection.KEY_DIGITAL_EFFECT)) {
                this.itemEntity.setTitleName(this.context.getResources().getString(R.string.x8_camera_digita));
                String[] sizeArray4 = this.context.getResources().getStringArray(R.array.x8_colours_array);
                this.itemEntity.setParamKey(CameraJsonCollection.KEY_DIGITAL_EFFECT);
                this.itemEntity.setOptions(new ArrayList());
                for (int i = 0; i < sizeArray4.length - 1; i++) {
                    this.itemEntity.getOptions().add(sizeArray4[i]);
                }
                this.itemEntity.getOptions().add(0, this.context.getResources().getString(R.string.x8_camera_digita));
                this.itemEntity.setParamValue(this.paramMap.get(CameraJsonCollection.KEY_DIGITAL_EFFECT));
                this.cameraManager.getCameraKeyOptions(CameraJsonCollection.KEY_DIGITAL_EFFECT, this);
                this.subParamsController.setSubParam(this.itemEntity);
            } else if (key.equals(CameraJsonCollection.KEY_CAMERA_STYLE)) {
                this.itemEntity.setTitleName(this.context.getResources().getString(R.string.x8_camera_style));
                String[] styleArray = this.context.getResources().getStringArray(R.array.x8_photo_style_array);
                this.itemEntity.setParamKey(CameraJsonCollection.KEY_CAMERA_STYLE);
                this.itemEntity.setOptions(new ArrayList());
                this.itemEntity.getOptions().add(0, this.itemEntity.getTitleName());
                for (String mode2 : styleArray) {
                    this.itemEntity.getOptions().add(mode2);
                }
                LinkedHashMap<String, String> map = new LinkedHashMap<>();
                map.put("saturation", this.paramMap.get("saturation"));
                map.put("contrast", this.paramMap.get("contrast"));
                this.itemEntity.setOptionMap(map);
                this.subParamsController.setSubParam(this.itemEntity);
            } else if (key.equals("video_quality")) {
                this.itemEntity.setTitleName(this.context.getResources().getString(R.string.x8_record_quality));
                String[] sizeArray5 = this.context.getResources().getStringArray(R.array.x8_record_quality_array);
                this.itemEntity.setParamKey("video_quality");
                this.itemEntity.setOptions(new ArrayList());
                for (String size4 : sizeArray5) {
                    this.itemEntity.getOptions().add(size4);
                }
                this.itemEntity.getOptions().add(0, this.context.getResources().getString(R.string.x8_record_quality));
                this.itemEntity.setParamValue(this.paramMap.get("video_quality"));
                this.subParamsController.setSubParam(this.itemEntity);
                this.cameraManager.getCameraKeyOptions("video_quality", this);
            } else if (key.equals("video_resolution")) {
                this.itemEntity.setParamKey("video_resolution");
                this.itemEntity.setParamValue(this.paramMap.get("video_resolution"));
                this.itemEntity.setTitleName(this.context.getResources().getString(R.string.x8_video_resolution));
                this.cameraManager.getCameraKeyOptions("video_resolution", this);
            } else if (key.equals("system_type")) {
                this.itemEntity.setTitleName(this.context.getResources().getString(R.string.x8_video_type));
                String[] sizeArray6 = this.context.getResources().getStringArray(R.array.x8_system_type_array);
                this.itemEntity.setParamKey("system_type");
                this.itemEntity.setOptions(new ArrayList());
                for (String size5 : sizeArray6) {
                    this.itemEntity.getOptions().add(size5);
                }
                this.itemEntity.getOptions().add(0, this.context.getResources().getString(R.string.x8_video_type));
                this.itemEntity.setParamValue(this.paramMap.get("system_type"));
                this.subParamsController.setSubParam(this.itemEntity);
                this.cameraManager.getCameraKeyOptions("system_type", this);
            } else if (key.equals(CameraJsonCollection.KEY_RECORD_AUTO_LOW_LIGHT)) {
            }
        } else if (key.equals(CameraJsonCollection.KEY_RECORD_MODE)) {
            String[] recordArray2 = this.context.getResources().getStringArray(R.array.x8_timelapse_record_array);
            this.itemEntity.setOptions(new ArrayList());
            for (String mode3 : recordArray2) {
                this.itemEntity.getOptions().add(mode3);
            }
            this.itemEntity.getOptions().add(0, this.context.getResources().getString(R.string.x8_record_mode));
            matchPhotoMode(this.itemEntity.getOptions(), this.itemEntity.getOptionMap());
            this.itemEntity.setParamKey(CameraJsonCollection.KEY_RECORD_MODE);
            this.itemEntity.setParamValue(this.paramMap.get(CameraJsonCollection.KEY_RECORD_MODE));
            this.itemEntity.setTitleName(this.context.getResources().getString(R.string.x8_record_mode));
            this.subParamsController.setSubParam(this.itemEntity);
        } else if (key.equals("capture_mode")) {
            String[] timelapseCaptureArray2 = this.context.getResources().getStringArray(R.array.x8_timelapse_capture_array);
            this.itemEntity.setOptions(new ArrayList());
            for (String timelapseCapture2 : timelapseCaptureArray2) {
                this.itemEntity.getOptions().add(timelapseCapture2);
            }
            this.itemEntity.getOptions().add(0, this.context.getResources().getString(R.string.x8_photo_mode));
            matchPhotoMode(this.itemEntity.getOptions(), this.itemEntity.getOptionMap());
            this.itemEntity.setParamKey("capture_mode");
            this.itemEntity.setTitleName(this.context.getResources().getString(R.string.x8_photo_mode));
            this.subParamsController.setSubParam(this.itemEntity);
        } else if (key.equals("photo_size")) {
            String[] sizeArray7 = this.context.getResources().getStringArray(R.array.x8_photo_size_array);
            this.itemEntity.setOptions(new ArrayList());
            for (String size6 : sizeArray7) {
                this.itemEntity.getOptions().add(size6);
            }
            this.itemEntity.getOptions().add(0, this.context.getResources().getString(R.string.x8_photo_size));
            this.itemEntity.setTitleName(this.context.getResources().getString(R.string.x8_photo_size));
            this.subParamsController.setSubParam(this.itemEntity);
        } else if (key.equals("photo_format")) {
            String[] formatArray = this.context.getResources().getStringArray(R.array.x8_photo_format_array);
            this.itemEntity.setOptions(new ArrayList());
            for (String fomat : formatArray) {
                this.itemEntity.getOptions().add(fomat);
            }
            this.itemEntity.getOptions().add(0, this.context.getResources().getString(R.string.x8_photo_format));
            this.itemEntity.setTitleName(this.context.getResources().getString(R.string.x8_photo_format));
            this.subParamsController.setSubParam(this.itemEntity);
        } else if (key.equals("awb")) {
            String[] awbArray = this.context.getResources().getStringArray(R.array.x8_awb_array);
            this.itemEntity.setOptions(new ArrayList());
            for (String awb : awbArray) {
                this.itemEntity.getOptions().add(awb);
            }
            this.itemEntity.getOptions().add(0, this.context.getResources().getString(R.string.x8_camera_awb));
            this.itemEntity.setTitleName(this.context.getResources().getString(R.string.x8_camera_awb));
            this.subParamsController.setSubParam(this.itemEntity);
        } else if (key.equals(CameraJsonCollection.KEY_METERMING_MODE)) {
            String[] meterArray = this.context.getResources().getStringArray(R.array.x8_meter_array);
            this.itemEntity.setOptions(new ArrayList());
            for (String meter : meterArray) {
                this.itemEntity.getOptions().add(meter);
            }
            this.itemEntity.getOptions().add(0, this.context.getResources().getString(R.string.x8_camera_metering));
            this.itemEntity.setTitleName(this.context.getResources().getString(R.string.x8_camera_metering));
            this.subParamsController.setSubParam(this.itemEntity);
        } else if (key.equals(CameraJsonCollection.KEY_DIGITAL_EFFECT)) {
            String[] colorArray = this.context.getResources().getStringArray(R.array.x8_colours_array);
            this.itemEntity.setOptions(new ArrayList());
            for (String color : colorArray) {
                this.itemEntity.getOptions().add(color);
            }
            this.itemEntity.getOptions().add(0, this.context.getResources().getString(R.string.x8_camera_digita));
            this.itemEntity.setTitleName(this.context.getResources().getString(R.string.x8_camera_digita));
            this.subParamsController.setSubParam(this.itemEntity);
        } else if (key.equals(CameraJsonCollection.KEY_CAMERA_STYLE)) {
            this.itemEntity.setTitleName(this.context.getResources().getString(R.string.x8_camera_style));
            String[] styleArray2 = this.context.getResources().getStringArray(R.array.x8_photo_style_array);
            this.itemEntity.setParamKey(CameraJsonCollection.KEY_CAMERA_STYLE);
            this.itemEntity.setOptions(new ArrayList());
            this.itemEntity.getOptions().add(0, this.itemEntity.getTitleName());
            for (String mode4 : styleArray2) {
                this.itemEntity.getOptions().add(mode4);
            }
            LinkedHashMap<String, String> map2 = new LinkedHashMap<>();
            map2.put("saturation", this.paramMap.get("saturation"));
            map2.put("contrast", this.paramMap.get("contrast"));
            this.itemEntity.setOptionMap(map2);
            this.subParamsController.setSubParam(this.itemEntity);
        } else if (key.equals("video_quality")) {
            String[] qualityArray = this.context.getResources().getStringArray(R.array.x8_record_quality_array);
            this.itemEntity.setOptions(new ArrayList());
            for (String quality : qualityArray) {
                this.itemEntity.getOptions().add(quality);
            }
            this.itemEntity.getOptions().add(0, this.context.getResources().getString(R.string.x8_record_quality));
            this.itemEntity.setParamKey("video_quality");
            this.itemEntity.setTitleName(this.context.getResources().getString(R.string.x8_record_quality));
            this.subParamsController.setSubParam(this.itemEntity);
        } else if (key.equals("video_resolution")) {
            String[] resolutionArray = this.context.getResources().getStringArray(R.array.x8_ntsc_resolution_array);
            this.itemEntity.setOptions(new ArrayList());
            for (String resolutio : resolutionArray) {
                this.itemEntity.getOptions().add(resolutio);
            }
            this.itemEntity.getOptions().add(0, this.context.getResources().getString(R.string.x8_video_resolution));
            matchVideoResolution(this.itemEntity.getOptions(), this.itemEntity.getOptionMap());
            this.itemEntity.setParamKey("video_resolution");
            this.itemEntity.setTitleName(this.context.getResources().getString(R.string.x8_video_resolution));
            this.subParamsController.setSubParam(this.itemEntity);
        } else if (key.equals("system_type")) {
            String[] typeArray = this.context.getResources().getStringArray(R.array.x8_system_type_array);
            this.itemEntity.setOptions(new ArrayList());
            for (String type : typeArray) {
                this.itemEntity.getOptions().add(type);
            }
            this.itemEntity.getOptions().add(0, this.context.getResources().getString(R.string.x8_video_type));
            this.itemEntity.setParamKey("system_type");
            this.itemEntity.setTitleName(this.context.getResources().getString(R.string.x8_video_type));
            this.subParamsController.setSubParam(this.itemEntity);
        }
        this.paramController.closeUi();
        this.subParamsController.openUi();
    }

    @Override
    public void itemReturnBack(String paramKey, String... paramValue) {
        this.paramController.openUi();
        this.subParamsController.closeUi();
        if (this.tokenEnable) {
            if (paramKey != null) {
                if (paramKey.equals(CameraJsonCollection.KEY_CAMERA_STYLE)) {
                    this.paramMap.put("saturation", paramValue[0]);
                    this.paramMap.put("contrast", paramValue[1]);
                } else {
                    this.paramMap.put(paramKey, paramValue[0]);
                }
            }
            this.paramController.updateMode(CameraParamStatus.modelStatus, this.paramMap);
            if (this.mainSetListener != null) {
                this.mainSetListener.initOptionsValue();
            }
        }
    }

    public void initData(JSONObject rtJson) {
        JSONArray paramArray;
        if (rtJson != null && rtJson.get("param") != null && (paramArray = rtJson.getJSONArray("param")) != null && paramArray.size() > 0) {
            X8CameraParamsValue paramsValue = X8CameraParamsValue.getInstance();
            for (int j = 0; j < paramArray.size(); j++) {
                JSONObject object = paramArray.getJSONObject(j);
                if (object != null) {
                    if (object.containsKey("video_quality")) {
                        this.paramMap.put("video_quality", object.getString("video_quality"));
                        paramsValue.getCurParamsJson().setVideo_quality(object.getString("video_quality"));
                    } else if (object.containsKey("video_resolution")) {
                        this.paramMap.put("video_resolution", object.getString("video_resolution"));
                        paramsValue.getCurParamsJson().setVideo_resolution(object.getString("video_resolution"));
                    } else if (object.containsKey(CameraJsonCollection.KEY_VIDEO_TIMELAPSE)) {
                        this.paramMap.put(CameraJsonCollection.KEY_VIDEO_TIMELAPSE, object.getString(CameraJsonCollection.KEY_VIDEO_TIMELAPSE));
                        paramsValue.getCurParamsJson().setVideo_timelapse(object.getString(CameraJsonCollection.KEY_VIDEO_TIMELAPSE));
                    } else if (object.containsKey(CameraJsonCollection.KEY_PHOTO_TIMELAPSE)) {
                        this.paramMap.put(CameraJsonCollection.KEY_PHOTO_TIMELAPSE, object.getString(CameraJsonCollection.KEY_PHOTO_TIMELAPSE));
                        paramsValue.getCurParamsJson().setPhoto_timelapse(object.getString(CameraJsonCollection.KEY_PHOTO_TIMELAPSE));
                    } else if (object.containsKey(CameraJsonCollection.KEY_RECORD_MODE)) {
                        this.paramMap.put(CameraJsonCollection.KEY_RECORD_MODE, object.getString(CameraJsonCollection.KEY_RECORD_MODE));
                        paramsValue.getCurParamsJson().setRecord_mode(object.getString(CameraJsonCollection.KEY_RECORD_MODE));
                    } else if (object.containsKey("capture_mode")) {
                        this.paramMap.put("capture_mode", object.getString("capture_mode"));
                        paramsValue.getCurParamsJson().setCapture_mode(object.getString("capture_mode"));
                    } else if (object.containsKey("photo_format")) {
                        this.paramMap.put("photo_format", object.getString("photo_format"));
                        paramsValue.getCurParamsJson().setPhoto_format(object.getString("photo_format"));
                    } else if (object.containsKey("photo_size")) {
                        this.paramMap.put("photo_size", object.getString("photo_size"));
                        paramsValue.getCurParamsJson().setPhoto_size(object.getString("photo_size"));
                    } else if (object.containsKey("ae_bias")) {
                        this.paramMap.put("ae_bias", object.getString("ae_bias"));
                        paramsValue.getCurParamsJson().setAe_bias(object.getString("ae_bias"));
                    } else if (object.containsKey(CameraJsonCollection.KEY_AE_ISO)) {
                        this.paramMap.put(CameraJsonCollection.KEY_AE_ISO, object.getString(CameraJsonCollection.KEY_AE_ISO));
                        paramsValue.getCurParamsJson().setIso(object.getString(CameraJsonCollection.KEY_AE_ISO));
                    } else if (object.containsKey(CameraJsonCollection.KEY_DE_CONTROL_TYPE)) {
                        this.paramMap.put(CameraJsonCollection.KEY_DE_CONTROL_TYPE, object.getString(CameraJsonCollection.KEY_DE_CONTROL_TYPE));
                        paramsValue.getCurParamsJson().setDe_control(object.getString(CameraJsonCollection.KEY_DE_CONTROL_TYPE));
                    } else if (object.containsKey(CameraJsonCollection.KEY_SHUTTER_TIME)) {
                        this.paramMap.put(CameraJsonCollection.KEY_SHUTTER_TIME, object.getString(CameraJsonCollection.KEY_SHUTTER_TIME));
                        paramsValue.getCurParamsJson().setShutter_time(object.getString(CameraJsonCollection.KEY_SHUTTER_TIME));
                    } else if (object.containsKey("awb")) {
                        this.paramMap.put("awb", object.getString("awb"));
                        paramsValue.getCurParamsJson().setAwb(object.getString("awb"));
                    } else if (object.containsKey(CameraJsonCollection.KEY_METERMING_MODE)) {
                        this.paramMap.put(CameraJsonCollection.KEY_METERMING_MODE, object.getString(CameraJsonCollection.KEY_METERMING_MODE));
                        paramsValue.getCurParamsJson().setMetering_mode(object.getString(CameraJsonCollection.KEY_METERMING_MODE));
                    } else if (object.containsKey(CameraJsonCollection.KEY_DIGITAL_EFFECT)) {
                        this.paramMap.put(CameraJsonCollection.KEY_DIGITAL_EFFECT, object.getString(CameraJsonCollection.KEY_DIGITAL_EFFECT));
                        paramsValue.getCurParamsJson().setDigital_effect(object.getString(CameraJsonCollection.KEY_DIGITAL_EFFECT));
                    } else if (object.containsKey("saturation")) {
                        this.paramMap.put("saturation", object.getString("saturation"));
                        paramsValue.getCurParamsJson().setSaturation(object.getString("saturation"));
                    } else if (object.containsKey("contrast")) {
                        this.paramMap.put("contrast", object.getString("contrast"));
                        paramsValue.getCurParamsJson().setContrast(object.getString("contrast"));
                    } else if (object.containsKey("sharpness")) {
                        this.paramMap.put("sharpness", object.getString("sharpness"));
                        paramsValue.getCurParamsJson().setSharpness(object.getString("sharpness"));
                    } else if (object.containsKey("system_type")) {
                        this.paramMap.put("system_type", object.getString("system_type"));
                        paramsValue.getCurParamsJson().setSystem_type(object.getString("system_type"));
                    } else if (object.containsKey(CameraJsonCollection.KEY_CAMERA_STYLE)) {
                        this.paramMap.put(CameraJsonCollection.KEY_CAMERA_STYLE, object.getString(CameraJsonCollection.KEY_CAMERA_STYLE));
                    }
                }
            }
        }
        if (this.paramController != null) {
            this.paramController.updateMode(CameraParamStatus.modelStatus, this.paramMap);
        }
        this.tokenEnable = true;
    }

    @Override
    public void openUi() {
        super.openUi();
        this.paramController.openUi();
        if (this.subParamsController != null) {
            this.subParamsController.closeUi();
        }
    }

    @Override
    public void closeUi() {
        super.closeUi();
        if (this.subParamsController != null) {
            this.subParamsController.gotoParentItem();
        }
        reviewData();
    }

    @Override
    public void onComplete(JSONObject rt, Object o) {
        if (rt != null) {
            CameraParamsJson paramsJson = JSON.toJavaObject(rt, CameraParamsJson.class);
            String paramType = paramsJson.getParam();
            if (paramsJson != null && paramType != null) {
                if (paramsJson.getMsg_id() == 9) {
                    HostLogBack.getInstance().writeLog("Alanqiu onComplete ++++0" + paramsJson);
                    List<String> mlist = paramsJson.getOptions();
                    mlist.add(0, this.itemEntity.getTitleName());
                    if (paramType.equals(CameraJsonCollection.KEY_SHUTTER_TIME)) {
                        this.itemEntity.setParamKey(CameraJsonCollection.KEY_SHUTTER_TIME);
                    } else if (paramType.equals("photo_format")) {
                        this.itemEntity.clearOptions();
                        this.itemEntity.setParamKey("photo_format");
                        this.itemEntity.setOptions(mlist);
                    } else if (paramType.equals("photo_size")) {
                        this.itemEntity.clearOptions();
                        this.itemEntity.setParamKey("photo_size");
                        this.itemEntity.setOptions(mlist);
                        HostLogBack.getInstance().writeLog("Alanqiu onComplete ++++111" + paramsJson);
                    } else if (paramType.equals("awb")) {
                        this.itemEntity.clearOptions();
                        this.itemEntity.setParamKey("awb");
                        this.itemEntity.setOptions(mlist);
                    } else if (paramType.equals(CameraJsonCollection.KEY_METERMING_MODE)) {
                        this.itemEntity.clearOptions();
                        this.itemEntity.setParamKey(CameraJsonCollection.KEY_METERMING_MODE);
                        this.itemEntity.setOptions(mlist);
                    } else if (paramType.equals(CameraJsonCollection.KEY_DIGITAL_EFFECT)) {
                        this.itemEntity.clearOptions();
                        this.itemEntity.setParamKey(CameraJsonCollection.KEY_DIGITAL_EFFECT);
                        this.itemEntity.setOptions(mlist);
                    } else if (paramType.equals("system_type") || paramsJson.equals("system_type")) {
                        this.itemEntity.clearOptions();
                        this.itemEntity.setParamKey("system_type");
                        this.itemEntity.setOptions(mlist);
                    } else if (paramType.equals("video_quality")) {
                        this.itemEntity.clearOptions();
                        this.itemEntity.setParamKey("video_quality");
                        this.itemEntity.setOptions(mlist);
                    } else if (paramType.equals("video_resolution")) {
                        this.itemEntity.setParamKey("video_resolution");
                    } else if (paramType.equals(CameraJsonCollection.KEY_RECORD_MODE)) {
                        String[] modeArray = this.context.getResources().getStringArray(R.array.x8_record_mode_array);
                        mlist.clear();
                        mlist.add(0, this.itemEntity.getTitleName());
                        Collections.addAll(mlist, modeArray);
                        this.itemEntity.setOptions(mlist);
                    } else if (paramType.equals("capture_mode")) {
                        String[] modeArray2 = this.context.getResources().getStringArray(R.array.x8_photo_mode_array);
                        mlist.clear();
                        mlist.add(0, this.itemEntity.getTitleName());
                        Collections.addAll(mlist, modeArray2);
                        this.itemEntity.setOptions(mlist);
                    } else if (paramType.equals(CameraJsonCollection.KEY_CAMERA_STYLE)) {
                        String[] styleArray = this.context.getResources().getStringArray(R.array.x8_photo_style_array);
                        mlist.clear();
                        mlist.add(0, this.itemEntity.getTitleName());
                        Collections.addAll(mlist, styleArray);
                        this.itemEntity.setOptions(mlist);
                    }
                    List<String> options = paramsJson.getOptions();
                    if (paramType.equals(CameraJsonCollection.KEY_METERMING_MODE) && options != null && options.size() > 0) {
                        for (int m = options.size() - 1; m >= 0; m--) {
                            String val = options.get(m);
                            if (val.equalsIgnoreCase(this.context.getResources().getString(R.string.x8_meter_roi))) {
                                options.remove(m);
                            } else if (val.equalsIgnoreCase(this.context.getResources().getString(R.string.x8_colours_flog)) && !StateManager.getInstance().isIs4KResolution()) {
                                options.remove(m);
                            }
                        }
                    }
                    this.itemEntity.setOptions(options);
                    matchNickKey(options);
                }
                if (this.subParamsController != null && this.itemEntity != null) {
                    this.subParamsController.setSubParam(this.itemEntity);
                }
            }
        }
    }

    private void matchNickKey(List<String> options) {
        LinkedHashMap<String, String> optionMap = new LinkedHashMap<>();
        if (this.itemEntity.getParamKey().equals("awb")) {
            for (String optionKey : options) {
                if (optionKey.equals(CameraJsonCollection.KEY_DE_CONTROL_AUTO)) {
                    optionMap.put(optionKey, this.context.getResources().getString(R.string.x8_awb_auto));
                } else if (optionKey.equals("incandescent")) {
                    optionMap.put(optionKey, this.context.getResources().getString(R.string.x8_awb_incandescent));
                } else if (optionKey.equals("d4000")) {
                    optionMap.put(optionKey, this.context.getResources().getString(R.string.x8_awb_d4000));
                } else if (optionKey.equals("sunny")) {
                    optionMap.put(optionKey, this.context.getResources().getString(R.string.x8_awb_sunny));
                } else if (optionKey.equals("cloudy")) {
                    optionMap.put(optionKey, this.context.getResources().getString(R.string.x8_awb_cloudy));
                } else if (optionKey.equals("shadw")) {
                    optionMap.put(optionKey, this.context.getResources().getString(R.string.x8_awb_shadow));
                }
            }
        } else if (this.itemEntity.getParamKey().equals(CameraJsonCollection.KEY_METERMING_MODE)) {
            for (String optionKey2 : options) {
                if (optionKey2.equals("spot")) {
                    optionMap.put(optionKey2, this.context.getResources().getString(R.string.x8_meter_spot));
                } else if (optionKey2.equals("center")) {
                    optionMap.put(optionKey2, this.context.getResources().getString(R.string.x8_meter_center));
                } else if (optionKey2.equals("average")) {
                    optionMap.put(optionKey2, this.context.getResources().getString(R.string.x8_meter_average));
                }
            }
        } else if (this.itemEntity.getParamKey().equals(CameraJsonCollection.KEY_DIGITAL_EFFECT)) {
            for (String optionKey3 : options) {
                if (optionKey3.equals("General")) {
                    optionMap.put(optionKey3, this.context.getResources().getString(R.string.x8_colours_general));
                } else if (optionKey3.equals("Vivid")) {
                    optionMap.put(optionKey3, this.context.getResources().getString(R.string.x8_colours_vivid));
                } else if (optionKey3.equals("art")) {
                    optionMap.put(optionKey3, this.context.getResources().getString(R.string.x8_colours_art));
                } else if (optionKey3.equals("black/white")) {
                    optionMap.put(optionKey3, this.context.getResources().getString(R.string.x8_colours_black_white));
                } else if (optionKey3.equals("film")) {
                    optionMap.put(optionKey3, this.context.getResources().getString(R.string.x8_colours_film));
                } else if (optionKey3.equals("sepia")) {
                    optionMap.put(optionKey3, this.context.getResources().getString(R.string.x8_colours_sepia));
                } else if (optionKey3.equals("F-LOG")) {
                    optionMap.put(optionKey3, this.context.getResources().getString(R.string.x8_colours_flog));
                } else if (optionKey3.equals("punk")) {
                    optionMap.put(optionKey3, this.context.getResources().getString(R.string.x8_colours_punk));
                }
            }
        } else if (this.itemEntity.getParamKey().equals("video_quality")) {
            for (String optionKey4 : options) {
                if (optionKey4.equals("sfine")) {
                    optionMap.put(optionKey4, this.context.getResources().getString(R.string.x8_record_quality_sfine));
                } else if (optionKey4.equals("fine")) {
                    optionMap.put(optionKey4, this.context.getResources().getString(R.string.x8_record_quality_fine));
                } else if (optionKey4.equals("normal")) {
                    optionMap.put(optionKey4, this.context.getResources().getString(R.string.x8_record_quality_normal));
                }
            }
        } else if (this.itemEntity.getParamKey().equals("video_resolution")) {
            matchVideoResolution(options, optionMap);
        }
        this.itemEntity.setOptionMap(optionMap);
    }

    private void matchPhotoMode(List<String> options, Map<String, String> optionMap) {
        List<String> keyOptions = new ArrayList<>();
        for (int i = 0; i < options.size(); i++) {
            String optionKey = options.get(i);
            if (optionKey != null && !"".equals(optionKey)) {
                if (i == 0) {
                    keyOptions.add(optionKey);
                } else {
                    String[] values = optionKey.split("\\s+");
                    optionMap.put(optionKey, values[1]);
                    if (!keyOptions.contains(values[0])) {
                        keyOptions.add(values[0]);
                    }
                }
            }
        }
        this.itemEntity.setOptions(keyOptions);
    }

    private void matchVideoResolution(List<String> options, Map<String, String> optionMap) {
        List<String> keyOptions = new ArrayList<>();
        for (int i = 0; i < options.size(); i++) {
            String optionKey = options.get(i);
            if (optionKey != null && !"".equals(optionKey)) {
                if (i == 0) {
                    keyOptions.add(optionKey);
                } else {
                    String[] values = optionKey.split("\\s+");
                    optionMap.put(optionKey, values[1].replace("P", "FPS"));
                    if (!keyOptions.contains(values[0])) {
                        keyOptions.add(values[0]);
                    }
                }
            }
        }
        this.itemEntity.setOptions(keyOptions);
    }

    @Override
    public void onDroneConnected(boolean b) {
        super.onDroneConnected(b);
        if (this.tokenEnable != b) {
            this.tokenEnable = b;
        }
        if (this.paramController != null) {
            this.paramController.onDroneConnected(b);
        }
        if (this.subParamsController != null) {
            this.subParamsController.onDroneConnected(b);
        }
    }

    public void setMainSetListener(IX8CameraMainSetListener mainSetListener) {
        this.mainSetListener = mainSetListener;
    }

    private void reviewData() {
        CurParamsJson paramsJson;
        if (StateManager.getInstance().getCamera().getToken() > 0 && (paramsJson = X8CameraParamsValue.getInstance().getCurParamsJson()) != null && paramsJson != null) {
            this.paramMap.put("video_quality", paramsJson.getVideo_quality());
            this.paramMap.put("video_resolution", paramsJson.getVideo_resolution());
            this.paramMap.put(CameraJsonCollection.KEY_TIMELAPSE_PHOTO, paramsJson.getPhoto_timelapse());
            this.paramMap.put(CameraJsonCollection.KEY_TIMELAPSE_VIDEO, paramsJson.getVideo_timelapse());
            this.paramMap.put("capture_mode", paramsJson.getCapture_mode());
            this.paramMap.put(CameraJsonCollection.KEY_RECORD_MODE, paramsJson.getRecord_mode());
            this.paramMap.put("photo_format", paramsJson.getPhoto_format());
            this.paramMap.put("photo_size", paramsJson.getPhoto_size());
            this.paramMap.put("ae_bias", paramsJson.getAe_bias());
            this.paramMap.put(CameraJsonCollection.KEY_AE_ISO, paramsJson.getIso());
            this.paramMap.put(CameraJsonCollection.KEY_SHUTTER_TIME, paramsJson.getShutter_time());
            this.paramMap.put("awb", paramsJson.getAwb());
            this.paramMap.put(CameraJsonCollection.KEY_METERMING_MODE, paramsJson.getMetering_mode());
            this.paramMap.put(CameraJsonCollection.KEY_DIGITAL_EFFECT, paramsJson.getDigital_effect());
            this.paramMap.put("saturation", paramsJson.getSaturation());
            this.paramMap.put("contrast", paramsJson.getContrast());
            this.paramMap.put("sharpness", paramsJson.getSharpness());
            this.paramMap.put("system_type", paramsJson.getSystem_type());
            if (this.paramController != null) {
                this.paramController.updateMode(CameraParamStatus.modelStatus, this.paramMap);
            }
        }
    }

    @Override
    public boolean onClickBackKey() {
        return false;
    }
}
