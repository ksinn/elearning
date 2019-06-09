/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */



$(document).ready(function(){

                $("#area").validate({

                   rules:{ 

                        name:{
                            required: true,
                            minlength: 4,
                            maxlength: 16,
                        },

                        picture:{
                            required: true,
                            accept: "png|jpg|jpeg",
                        },
                        
                   },

                   messages:{

                        name:{
                            required: "Это поле обязательно для заполнения",
                            minlength: "Название должен быть минимум 4 символа",
                            maxlength: "Максимальное число символо - 16",
                        },

                        picture:{
                            required: "Это поле обязательно для заполнения",
                        },

                   }

                });


            }); //end of ready
