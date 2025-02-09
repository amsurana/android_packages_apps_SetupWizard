/*
 * Copyright (C) 2016 The CyanogenMod Project
 * 
 *
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
 */

package com.android.initwizard;

import static android.content.Intent.FLAG_GRANT_READ_URI_PERMISSION;

import static com.android.initwizard.SetupWizardApp.ACTION_LOAD;
import static com.android.initwizard.SetupWizardApp.EXTRA_SCRIPT_URI;
import static com.android.initwizard.SetupWizardApp.LOGV;

import android.annotation.Nullable;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.android.setupwizardlib.util.WizardManagerHelper;

import com.android.initwizard.util.SetupWizardUtils;
import com.android.initwizard.wizardmanager.WizardManager;

public class SetupWizardActivity extends BaseSetupWizardActivity {
    private static final String TAG = SetupWizardActivity.class.getSimpleName();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (LOGV) {
            Log.v(TAG, "onCreate savedInstanceState=" + savedInstanceState);
        }
        if (SetupWizardUtils.hasGMS(this)) {
            SetupWizardUtils.disableHome(this);
            finish();
        } else if (WizardManagerHelper.isUserSetupComplete(this)) {
            SetupWizardUtils.finishSetupWizard(this);
            finish();
        } else {
            onSetupStart();
            SetupWizardUtils.enableComponent(this, WizardManager.class);
            Intent intent = new Intent(ACTION_LOAD);
            if (isPrimaryUser()) {
                intent.putExtra(EXTRA_SCRIPT_URI, getString(R.string.android_wizard_script_uri));
            } else {
                intent.putExtra(EXTRA_SCRIPT_URI, getString(R.string.android_wizard_script_user_uri));
            }
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | FLAG_GRANT_READ_URI_PERMISSION);
            startActivity(intent);
            finish();
        }
    }
}
