/*
 * Copyright 2021 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.androiddevchallenge

import android.content.Context
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.*
import androidx.navigation.compose.*
import com.example.androiddevchallenge.ui.theme.MyTheme
import com.example.androiddevchallenge.ui.theme.shapes
import com.example.androiddevchallenge.ui.theme.teal200
import com.google.gson.Gson

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyTheme {
                MyApp()
            }
        }
    }
}

// Start building your app here!
data class Dog(var num: Int, var name: String, var age: String)

val gson = Gson()

@Composable
fun MyApp() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "list") {
        composable("list") {

            DogList(navController)
        }
        composable(
            "detail/{dog}",
            arguments = listOf(navArgument("dog") { type = NavType.StringType })
        ) { backStackEntry ->

            DogDetail(gson.fromJson(backStackEntry.arguments?.getString("dog"), Dog::class.java))
        }
    }

}

@Composable
fun DogDetail(dog: Dog) {

    Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
        Card(
            modifier = Modifier
                .padding(16.dp)
                .width(200.dp)
                .height(200.dp)
                .fillMaxWidth()
        ) {
            Image(
                painter = painterResource(id = "ia_${dog.num}".drawableId),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight()
            )

        }
        Text(
            text = dog.name,
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            fontStyle = FontStyle.Italic,
            fontFamily = FontFamily.SansSerif
        )
        Row(verticalAlignment = Alignment.Bottom) {
            Text(text = dog.age, fontSize = 18.sp, fontWeight = FontWeight.Bold)
            Text(text = " Years Old", fontSize = 12.sp)
        }
    }
}

@Composable
fun DogList(navController: NavController) {
    val dogstring = MyApplication.application?.assets?.open("dogs.json")?.use {
        String(it.readBytes())
    } ?: ""
    val dogs = gson.fromJson(dogstring, Array<Dog>::class.java)
    Surface(color = teal200) {
        LazyColumn {
            items(dogs) {
                DogItemView(it) {
                    navController.navigate(
                        "detail/${gson.toJson(it)}"
                    ) {

                    }
                }

            }

        }
    }
}

val String.drawableId: Int
    get() = MyApplication.application?.let {
        it.resources?.getIdentifier(this, "drawable", it.packageName) ?: 0
    } ?: 0


@Composable
fun DogItemView(dog: Dog, click: (Dog) -> Unit) {
    Card(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
    ) {
        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier
            .clickable {

                click(dog)
            }
            .fillMaxWidth()) {
            Card(
                modifier = Modifier
                    .width(120.dp)
                    .height(120.dp)
                    .padding(10.dp), shape = shapes.large
            ) {
                Image(
                    painter = painterResource(id = "ia_${dog.num}".drawableId),
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight()
                )
            }
            Column {
                Row(verticalAlignment = Alignment.Bottom) {
                    Text(text = "Name:")
                    Text(
                        text = "${dog.name}",
                        color = Color.Black,
                        fontSize = 20.sp,
                        maxLines = 1,
                        fontWeight = FontWeight.Bold
                    )

                }
                Text(text = "Age:${dog.age}", fontSize = 16.sp)
            }

        }

    }
}


@Preview("Light Theme", widthDp = 360, heightDp = 640)
@Composable
fun LightPreview() {
    MyTheme {
        MyApp()
    }
}

//@Preview("Dark Theme", widthDp = 360, heightDp = 640)
@Composable
fun DarkPreview() {
    MyTheme(darkTheme = true) {
        MyApp()
    }
}
