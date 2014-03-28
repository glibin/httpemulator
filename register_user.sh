#!/bin/bash
#Creating users Mail.ru, JTB, VK, OK

#----------------------------variables---------------------------------
interactive_var="false" #if we are interactive
areThereSomeArgs="false" #check if arg list is empty

action_var="NULL" #what are we going to do CREATE/DELETE
system_var="NULL" #in which system we want to create a user

action_array_var=('CREATE' 'DELETE') #avaliable actions
system_array_var=('mailru' 'tutby' 'vk' 'ok') #avalibale systems

mailru_or_jtb_login_var=""

central_auth_proxy_var="http://jenkins.pyn.ru:9338"
central_http_emulator_create_var="http://jenkins.pyn.ru:18880/criteria/simple"
central_http_emulator_delete_var="NUULLLLLLL"

stand_var=$HOSTNAME
stand_var='mercury' #!!!!DELETE THIS LINE AFTER TESTED

site_var='hh.ru'
#------------------------MODIFIABLES----------------------
first_name="" #Make  random name
last_name=""
sex="male" #нихрена не работает, и нет времени разбираться почему
bdate="14.03.1955"

#--------------------------parsing options-----------------------------
#Parsing init options and argument values

while getopts ":his:l:" optname
  do
    case "$optname" in
      "i")
        interactive_var="true"
	areThereSomeArgs="true"
	break
	;;
     "l")
        mailru_or_jtb_login_var=$OPTARG
        areThereSomeArgs="true"
        ;;
      "s")
        if [[ "${system_array_var[*]}" =~ "$OPTARG" ]]
        then
                system_var=$OPTARG
		areThereSomeArgs="true"
        else
                echo -e "\e[1;31mSystem - $OPTARG is not supported\e[1;0m"
		exit 1
        fi
        ;;
      "?")
        echo -e "\e[1;31mUnknown option $OPTARG\e[1;0m"
	exit 1
        ;;
      ":")
        echo -e "\e[1;31mNo argument value for option $OPTARG\e[1;0m"
	exit 1
        ;;
      *)
      # Should not occur
        echo -e "\e[1;31mUnknown error while processing options\e[1;0m"
        ;;
    esac
done

#--------------------------HELP------------------------------------
if [[ "$areThereSomeArgs" == "false" ]]
then
        echo -e "\e[1;33m----------------------------------------HELP PAGE-------------------------------------------"
        echo -e "|   OPTIONS:                                                                               -"
        echo -e "|    -i   INTERACTIVE MODE                                                                 -"
        echo -e "|    -s   SYSTEM. Может принимать значения: mailru, tutby, vk, (ok, fb, li, gp недоступны) -"
        echo -e "|    -l   LOGIN. Login для mailru и tutby                                                  -"
        echo -e "--------------------------------------------------------------------------------------------\e[1;0m"
else

#--------------------IF WE HAVE SOME OPTIONS-----------------------------

#---------------------------Interative-----------------------------------

if [[ "$interactive_var" == "true" ]]
then
 echo -e "\e[1;32m----------Вы используете интерактивный режим----------\e[1;0m"
 exit_flag_var=""
 counter=1
 while [[ "$exit_flag_var" != "exit" ]]
 do 
	let counter++
	read -p "Создать юзера (${system_array_var[*]}):" system_var
	system_var=$(echo "$system_var" | tr -d ' ')
	if [[ "${system_array_var[*]}" =~ "$system_var" ]] && [ ! -z "$system_var" ]
	then
		exit_flag_var="exit"
	fi
        if [ $counter -eq 5 ]
	then
		echo -e "\e[1;31mДА БЛИН ВВЕДИ УЖЕ НАКОНЕЦ ПРАВИЛЬНОЕ ЗНАЧЕНИЕ.......\e[1;0m"
	fi
 done
 
 if [[ "$system_var" == "tutby" ]] || [[ "$system_var" == "mailru" ]]
 then
	read -p "Введите логин:" mailru_or_jtb_login_var
        mailru_or_jtb_login_var=$(echo "$mailru_or_jtb_login_var" | tr -d ' ')

        if [ "$mailru_or_jtb_login_var" == "" ]
        then
                 mailru_or_jtb_login_var=$(cat /dev/urandom | tr -dc 'a-zA-Z' | fold -w 12 | head -n 1)
        fi


 else
 	read -p "Введите имя пользователя:" first_name
 	first_name=$(echo "$first_name" | tr -d ' ')
 
 	if [ "$first_name" == "" ]
 	then
   		 first_name=$(cat /dev/urandom | tr -dc 'a-zA-Z' | fold -w 12 | head -n 1)
 	fi
 
 

 	read -p "Введите фамилию пользователя:" last_name
 	last_name=$(echo "$last_name" | tr -d ' ')
 	if [ "$last_name" == "" ]
 	then
    		last_name=$(cat /dev/urandom | tr -dc 'a-zA-Z' | fold -w 12 | head -n 1)
 	fi
 
 fi #--end of the if tutby or mailru

fi

#---------------------------CREATE-------------------------------------


   if [[ "$system_var" == "NULL" ]]
   then
      echo -e "\e[1;31mУкажите систему, используйте опцию -s\e[1;0m "
      exit 1	
   fi
  #-------------------if we want to create VK user-------------------------
   if [[ "$system_var" == "vk" ]]
   then
	#echo "Creating VK user"
	#Generating variables to ID our requiest
	vkRequestId="VKREQUESTID"$(cat /dev/urandom | tr -dc 'a-zA-Z0-9' | fold -w 64 | head -n 1)"VKREQUESTID" #NOTE THIS ONE IS USED AS COOkIE VALUE TO ID a USER
	vkCode="VKCODE"$(cat /dev/urandom | tr -dc 'a-zA-Z0-9' | fold -w 64 | head -n 1)"VKCODE"
	vkToken="VKTOKEN"$(cat /dev/urandom | tr -dc 'a-zA-Z0-9' | fold -w 64 | head -n 1)"VKTOKEN"
	uidVK="1"$(cat /dev/urandom | tr -dc '0-9' | fold -w 10 | head -n 1) #11 digits starts with 1

	#MODIFY THIS TO ALTER USER IMPORT DATA-------------------!!!!!!!!!!
	if [[ "$first_name" == "" ]]
	then
		first_name=$(cat /dev/urandom | tr -dc 'a-zA-Z' | fold -w 12 | head -n 1)
	fi
	if [[ "$last_name" == "" ]]
	then
		last_name=$(cat /dev/urandom | tr -dc 'a-zA-Z' | fold -w 12 | head -n 1)
	fi
	

	#stand and cite
	#site_var='hh.ru' #NOTE ----LOOP FOR ALL THE SITES???????

	#-------------first request params for VK----------------------
	rule1='rule={"id":null,"type":"COOKIE","key":"OAUTH-REQUEST-ID","value":"'$vkRequestId'"}'
	response1='response=[{"id":null,"type":"STATUS","key":null,"value":"302"},{"id":null,"type":"HEADER","key":"Location","value":"http://hhid.'$stand_var'.pyn.ru/oauth2/code?reg=http%3A%2F%2F'$site_var'.'$stand_var'.pyn.ru%2Foauth%2Fcallback%3Freg%3Doauth2%26fail=http%3A%2F%2F'$site_var'.'$stand_var'.pyn.ru%2Foauth%2Fcallback%26login=http%3A%2F%2F'$site_var'.'$stand_var'.pyn.ru%2Foauth%2Fcallback%3Furl%3D'$site_var'.'$stand_var'.pyn.ru%26system=VK%26mergeOAuth=false%26code='$vkCode'%26error=%26error_description="}]'

	#-------------second request params for VK
	rule2='rule={"id":null,"type":"PARAMETER","key":"code","value":"'$vkCode'"}'
	response2='response=[{"id":null,"type":"STATUS","key":null,"value":"200"},{"id":null,"type":"BODY","key":null,"value":"{\"access_token\":\"'$vkToken'\",\"user_id\":'$uidVK'}"}]'

	#-------------third request params for VK
	rule3='rule={"id":null,"type":"PARAMETER","key":"access_token","value":"'$vkToken'"}'
	response3='response=[{"id":null,"type":"STATUS","key":null,"value":"200"},{"id":null,"type":"BODY","key":null,"value":"{\"response\":{\"user\":[{\"uid\":\"'$uidVK'\",\"first_name\":\"'$first_name'\",\"last_name\":\"'$last_name'\",\"sex\":\"'$sex'\",\"bdate\":\"'$bdate'\",\"city\":\"1386\",\"country\":\"1\",\"personal\":{\"langs\":[\"Русский\",\"Английский\"]},\"universities\":[{\"id\":\"239\",\"country\":\"1\",\"city\":\"1386\",\"name\":\"БГУ\",\"faculty\":\"959\",\"faculty_name\":\"Факультетприкладныхисскуств\",\"chair\":\"18134\",\"chair_name\":\"Изобразительноеисскуство\",\"graduation\":\"2010\",\"education_form\":\"Дневноеотделение\",\"education_status\":\"Выпускникспециалист\"}],\"fullName\":\"LnbdjfidjeicagcihcFnbdjfidjeicagbiag\"}],\"countries\":[{\"cid\":\"1\",\"name\":\"Россия\"}],\"cities\":[{\"cid\":\"1386\",\"name\":\"Москва\"}]},\"firstNameUser\":\"'$first_name'\",\"lastNameUser\":\"'$last_name'\",\"fullNameUser\":\"'$first_name$last_name'\",\"universityNameUser\":\"БГУ\",\"facultyUniversityNameUser\":\"Факультетприкладныхисскуств\",\"cityLocalityNameUser\":\"Москва\",\"birthDayUser\":\"10.02.1990\",\"sexUser\":\"Мужской\",\"languagesUser\":[\"Русский\",\"Английский\"]}"}]'
	
		
	idRule1=$(curl -X PUT --data-urlencode "POKERFACE" -d $rule1 -d $response1 $central_http_emulator_create_var -s)
	idRule2=$(curl -X PUT --data-urlencode "POKERFACE" -d $rule2 -d $response2 $central_http_emulator_create_var -s)
	idRule3=$(curl -X PUT --data-urlencode "POKERFACE" -d $rule3 -d $response3 $central_http_emulator_create_var -s)

	#check if the value returned is an integer
	if [ $idRule1 -eq $idRule1 2> /dev/null ] && [ $idRule1 -eq $idRule1 2> /dev/null ] && [ $idRule1 -eq $idRule1 2> /dev/null ]
	then
		echo -e "\e[1;32m*****************************************************************************************************************************************************"
		echo -e "                        Юзер\e[1;34m Вконтакте\e[1;32m успешно создан                             \e[1;0m "
		echo -e "\e[1;31m Ваши дальнейшие шаги:                                                             "
		echo -e " 1)\e[1;33m Меняем хосты на тестовом стенде и на машине где будет запускаться браузер      "
		echo -e "    так чтобы \e[1;34mvkontakte.ru, api.vkontakte.ru, vk.com, oauth.vk.com\e[1;33m резолвились     "
		echo -e "    на машину где поднят http_emulator\e[1;31m                                             " 
		echo -e " 2)\e[1;33m Преходим в браузере на\e[1;34m https://oauth.vk.com\e[1;33m (страница будет пустая) и проставляем куку  "
		echo -e "    key:\"\e[1;34mOAUTH-REQUEST-ID\e[1;33m\", value:\"\e[1;34m$vkRequestId\e[1;33m\", ставим галочку \e[1;34msecure\e[1;33m. (удобно использовать Edit This Cookie)\e[1;31m   "
		echo -e " 3)\e[1;33m Переходим на сайт\e[1;34m $site_var\e[1;33m стенда\e[1;34m $stand_var\e[1;33m, жмем кнопку\e[1;34m Вконтакте\e[1;33m. (для другихъ сайтов не будет работать - если комуто надо могу доделать)                   "
		echo -e "    Логин и пароль вводить не надо, аутентификация происходит по куке     \e[1;31m "
		echo -e " 4) \e[1;33mTTL у созданных правил 10 часов                                      \e[1;31m "
		echo -e " 5)\e[1;33m VK ID созданного пользователя:\e[1;34m $uidVK\e[1;0m "
		echo "                                                                                   "
		echo -e "\e[1;32m*****************************************************************************************************************************************************\e[1;0m"
		echo "                                                                                   "
		echo -e "\e[1;33m На http_emilator успешно созданны следующие правила:\e[1;34m $idRule1, $idRule2, $idRule3 "
		echo -e "\e[1;33m Данные айдишники нужны для дальнейшего удаления правил из эмулятора.   \e[1;0m           "
		echo "                                                                                   "
		echo -e "\e[1;32m*****************************************************************************************************************************************************\e[1;0m"
	else
		echo -e "\e[1;31mОШИБКА ЮЗЕР НЕ СОЗДАН - ВСЕ ПОГИБЛО, ВСЕ ПРОПАЛО.... ПРОВЕРЬТЕ КОРРЕКТНОСТЬ URL\e[1;0m"
		echo $idRule1
		echo $idRule2
		echo $idRule3
		echo $(curl $response3 $central_http_emulator_create_var -v)
	fi

   fi
   #--------------------------VK USER CREATION ENDS HERE-------------------	

   #---------------------------CREATE MAILRU USER------------------------------
   if [[ "$system_var" == "mailru" ]]
   then
	if [[ "$mailru_or_jtb_login_var" != "" ]]
	then
	        pass=123
		curl "$central_auth_proxy_var/register_test_account?login=$mailru_or_jtb_login_var&type=$system_var&password=$pass"
		echo -e "\e[1;32m******************************************************************************************************************"
                echo -e "                                            Юзер Mail.ru успешно создан                             \e[1;0m "
                echo -e "\e[1;36m Login\e[1;0m:\e[1;32m$mailru_or_jtb_login_var\e[1;0m@mail.ru, \e[1;36mpassword\e[1;0m:\e[1;32m$pass\e[1;0m  "
                echo -e "\e[1;31m Хосты \e[1;36mauth.mail.ru connect.mail.ru www.appsmail.ru swa.mail.ru\e[1;31m на тестовом стенде должны резолвится на auth-proxy\e[1;0m "
		echo "       "
                echo -e "\e[1;31m FYI\e[1;0m Пользователя \e[1;36mmailru\e[1;0m можно зарегистрировать с помощью \e[1;34mcurl\e[1;0m (выделенное красным заменить): "
                echo -e "   \e[1;33m $central_auth_proxy_var/register_test_account?login=\e[1;31mLOGIN\e[1;33m&type=mailru&password=\e[1;31mPASSWORD\e[1;0m       " 
                echo -e "\e[1;32m******************************************************************************************************************\e[1;0m"

	else
		echo -e "\e[1;31mИспользуйте опцию \e[1;35m-l\e[1;31m чтобы указать желаемый логин!\e[1;0m"
	fi
   fi
   #---------------------------MAILRU CREATION ENDS HERE------------------------	
   #---------------------------CREATE JTB USER----------------------------------
   if [[ "$system_var" == "tutby" ]]
   then
       if [[ "$mailru_or_jtb_login_var" != "" ]]
        then
                pass=123
                curl "$central_auth_proxy_var/register_test_account?login=$mailru_or_jtb_login_var&type=$system_var&password=$pass"
                echo -e "\e[1;32m******************************************************************************************************************"
                echo -e "                                            Юзер JTB успешно создан                             \e[1;0m "
                echo -e "\e[1;36m Login\e[1;0m:\e[1;32m$mailru_or_jtb_login_var\e[1;0m, \e[1;36mpassword\e[1;0m:\e[1;32m$pass\e[1;0m  "
                echo -e "\e[1;31m Хосты \e[1;36mprofile.tut.by profile.tut.by.\e[1;31m на тестовом стенде должны резолвится на auth-proxy\e[1;0m "
		echo "       "
                echo -e "\e[1;31m FYI\e[1;0m Пользователя \e[1;36mtutby\e[1;0m можно зарегистрировать с помощью \e[1;34mcurl\e[1;0m (выделенное красным заменить): "
                echo -e "   \e[1;33m $central_auth_proxy_var/register_test_account?login=\e[1;31mLOGIN\e[1;33m&type=tutby&password=\e[1;31mPASSWORD\e[1;0m       " 
                echo -e "\e[1;32m******************************************************************************************************************\e[1;0m"
        else
                echo -e "\e[1;31mИспользуйте опцию \e[1;35m-l\e[1;31m чтобы указать желаемый логин!\e[1;0m"
        fi
   fi
   #---------------------------JTB CREATION ENDS HERE---------------------------

   #--------------------------CREATE ODNOGLAZZNIKI user--------------------------
   if [[ "$system_var" == "ok" ]]
   then
	#generating IDs to id our requests
	okRequestId="OKREQUESTID"$(cat /dev/urandom | tr -dc 'a-zA-Z0-9' | fold -w 64 | head -n 1)"OKREQUESTID" #NOTE THIS ONE IS USED AS COOkIE VALUE TO ID a USER
        okCode="OKCODE"$(cat /dev/urandom | tr -dc 'a-zA-Z0-9' | fold -w 64 | head -n 1)"OKCODE"
        okToken="OKTOKEN"$(cat /dev/urandom | tr -dc 'a-zA-Z0-9' | fold -w 64 | head -n 1)"OKTOKEN"
        uidOK="1"$(cat /dev/urandom | tr -dc '0-9' | fold -w 10 | head -n 1) #11 digits starts with 1

	#MODIFY THIS TO ALTER USER IMPORT DATA-------------------!!!!!!!!!!
        if [[ "$first_name" == "" ]]
        then
                first_name=$(cat /dev/urandom | tr -dc 'a-zA-Z' | fold -w 12 | head -n 1)
		#echo $first_name
        fi
    
        if [[ "$last_name" == "" ]]
        then
                last_name=$(cat /dev/urandom | tr -dc 'a-zA-Z' | fold -w 12 | head -n 1)
		#echo $last_name
        fi


	#------rule1 OK-------
	rule1='rule={"id":null,"type":"COOKIE","key":"OAUTH-REQUEST-ID","value":"'$okRequestId'"}'
	response1='response=[{"id":null,"type":"STATUS","key":null,"value":"302"},{"id":null,"type":"HEADER","key":"Location","value":"http://hhid.'$stand_var'.pyn.ru/oauth2/code?reg=http%3A%2F%2F'$site_var'.'$stand_var'.pyn.ru%2Foauth%2Fcallback%3Freg%3Doauth2%26fail=http%3A%2F%2F'$site_var'.'$stand_var'.pyn.ru%2Foauth%2Fcallback%26login=http%3A%2F%2F'$site_var'.'$stand_var'.pyn.ru%2Foauth%2Fcallback%3Furl%3D'$site_var'.'$stand_var'.pyn.ru%26system=OK%26mergeOAuth=false%26code='$okCode'%26error=%26error_description="}]'

        #---rule2 OK----
	rule2='rule={"id":null,"type":"PARAMETER","key":"code","value":"'$okCode'"}'
	response2='response=[{"id":null,"type":"STATUS","key":null,"value":"200"},{"id":null,"type":"BODY","key":null,"value":"{\"access_token\":\"'$okToken'\",\"user_id\":'$uidOK'}"}]'
  

	rule3='rule={"id":null,"type":"PARAMETER","key":"access_token","value":"'$okToken'"}'
	response3='response=[{"id":null,"type":"STATUS","key":null,"value":"200"},{"id":null,"type":"BODY","key":null,"value":"{\"uid\":\"'$uidOK'\",\"birthday\":\"10-02-1985\",\"age\":24,\"first_name\":\"'$first_name'\",\"last_name\":\"'$last_name'\",\"name\":\"MIDDLENAME\",\"locale\":\"ru\",\"gender\":\"'$sex'\",\"has_email\":true,\"location\":{\"countryCode\":\"RU\",\"country\":\"RUSSIAN_FEDERATION\",\"city\":\"Москва\"},\"online\":\"web\",\"photo_id\":\"891584623\",\"pic_1\":\"http%3A%2F%2Fi500.mycdn.me%2FgetImage?photoId=891584623%26photoType=4%26viewToken=rvpZfi6YG671x0TB6ncxjw\",\"pic_2\":\"http%3A%2F%2Fusd1.mycdn.me%2FgetImage?photoId=891584623%26photoType=2%26viewToken=rvpZfi6YG671x0TB6ncxjw\",\"email\":\"'$uidOK'%40odnoklassniki.ru\"}"}]'

	
	idRule1=$(curl -X PUT --data-urlencode "POKERFACE" -d $rule1 -d $response1 $central_http_emulator_create_var -s)
        idRule2=$(curl -X PUT --data-urlencode "POKERFACE" -d $rule2 -d $response2 $central_http_emulator_create_var -s)
        idRule3=$(curl -X PUT --data-urlencode "POKERFACE" -d $rule3 -d $response3 $central_http_emulator_create_var -s)

        #check if the value returned is an integer  AND CONDITION DOES NOT WORK !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
        if [ $idRule1 -eq $idRule1 2> /dev/null ] && [ $idRule1 -eq $idRule1 2> /dev/null ] && [ $idRule1 -eq $idRule1 2> /dev/null ]
        then
                echo -e "\e[1;32m*****************************************************************************************************************************************************"
                echo -e "                        Юзер\e[1;34m Одноклассники\e[1;32m успешно создан                             \e[1;0m "
                echo -e "\e[1;31m Ваши дальнейшие шаги:                                                             "
                echo -e " 1)\e[1;33m Меняем хосты на тестовом стенде и на машине где будет запускаться браузер      "
                echo -e "    так чтобы \e[1;34mapi.odnoklassniki.ru, odnoklassniki.ru, www.odnoklassniki.ru\e[1;33m резолвились     "
                echo -e "    на машину где поднят http_emulator\e[1;31m                                             " 
                echo -e " 2)\e[1;33m Преходим в браузере на\e[1;34m http://odnoklassniki.ru\e[1;33m (страница будет пустая) и проставляем куку  "
                echo -e "    key:\"\e[1;34mOAUTH-REQUEST-ID\e[1;33m\", value:\"\e[1;34m$okRequestId\e[1;33m\". (удобно использовать Edit This Cookie)\e[1;31m   "
                echo -e " 3)\e[1;33m Переходим на сайт\e[1;34m $site_var\e[1;33m стенда\e[1;34m $stand_var\e[1;33m, жмем кнопку\e[1;34m Одноклассники\e[1;33m. (для другихъ сайтов не будет работать - если комуто надо могу доделать)                   "
                echo -e "    Логин и пароль вводить не надо, аутентификация происходит по куке     \e[1;31m "
                echo -e " 4) \e[1;33mTTL у созданных правил 10 часов                                      \e[1;31m "
                echo -e " 5)\e[1;33m OK ID созданного пользователя:\e[1;34m $uidOK\e[1;0m "
                echo "                                                                                   "
                echo -e "\e[1;32m*****************************************************************************************************************************************************\e[1;0m"
                echo "                                                                                   "
                echo -e "\e[1;33m На http_emilator успешно созданны следующие правила:\e[1;34m $idRule1, $idRule2, $idRule3 "
                echo -e "\e[1;33m Данные айдишники нужны для дальнейшего удаления правил из эмулятора.   \e[1;0m           "
                echo "                                                                                   "
                echo -e "\e[1;32m*****************************************************************************************************************************************************\e[1;0m"
        else
                echo -e "\e[1;31mОШИБКА ЮЗЕР НЕ СОЗДАН - ВСЕ ПОГИБЛО, ВСЕ ПРОПАЛО.... ПРОВЕРЬТЕ КОРРЕКТНОСТЬ URL\e[1;0m"
                echo $idRule1
                echo $idRule2
                echo $idRule3
                echo $(curl $response3 $central_http_emulator_create_var -v)
        fi



   fi
   #--------------------------ODNOGLAZZNIKI CREATION ENDS HERE-------------------

#----------------------------------Creation---------------------------------------

fi #end of optarg check for size
