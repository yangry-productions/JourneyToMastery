/*
 * Copyright (C) 2008 Google Inc.
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

package com.android.yangryProductions.Sessions;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class SessionEdit extends Activity {

    private EditText mTitleText;
    private EditText mBodyText;
    private Long mRowId;
    private SessionDbAdapter mDbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mDbHelper = new SessionDbAdapter(this);
        mDbHelper.open();

        setContentView(R.layout.session_edit);

        mTitleText = (EditText) findViewById(R.id.title);
        mBodyText = (EditText) findViewById(R.id.body);

        Button confirmButton = (Button) findViewById(R.id.confirm);

        mRowId = (savedInstanceState==null)?null:
                (long)savedInstanceState.getSerializable(SessionDbAdapter.KEY_ROWID);
        if(mRowId==null){
            Bundle extras = getIntent().getExtras();
            mRowId = extras!=null?extras.getLong(SessionDbAdapter.KEY_ROWID)
                    :null;
        }

        populateFields();

        confirmButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                setResult(RESULT_OK);
                finish();
            }
        });
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        saveState();
        outState.putSerializable(SessionDbAdapter.KEY_ROWID, mRowId);
    }

    @Override
    protected void onPause() {
        super.onPause();
        saveState();
    }

    @Override
    protected void onResume() {
        super.onResume();
        populateFields();
    }

    private void populateFields(){
        if(mRowId!=null){
            Cursor session = mDbHelper.fetchSession(mRowId);
            mTitleText.setText(session.getString(session.getColumnIndexOrThrow(SessionDbAdapter.KEY_TITLE)));
            mBodyText.setText(session.getString(session.getColumnIndexOrThrow(SessionDbAdapter.KEY_BODY)));
        }
    }

    private void saveState(){
        String title = mTitleText.getText().toString();
        String body = mBodyText.getText().toString();
        if(mRowId==null){
            long id = mDbHelper.createSession(title, body);
            if(id>0){
                mRowId=id;
            }else{
                mDbHelper.updateSession(mRowId, title, body);
            }
        }
    }
}
