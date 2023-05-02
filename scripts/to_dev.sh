#! /bin/bash


# find ../dist/js/ -type f -path "*.js" | xargs sed -i 's:被替换的内容:新的内容:g'
find ../web/src/ -type f -path "*.vue" | xargs sed -i 's#http://127.0.0.1:10300#https://app3426.acapp.acwing.com.cn#g'
find ../web/src/ -type f -path "*.js" | xargs sed -i 's#http://127.0.0.1:10300#https://app3426.acapp.acwing.com.cn#g'
find ../web/src/ -type f -path "*.vue" | xargs sed -i 's#ws://127.0.0.1:10300#wss://app3426.acapp.acwing.com.cn#g'
find ../web/src/ -type f -path "*.vue" | xargs sed -i 's#http://127.0.0.1:8081#https://app3426.acapp.acwing.com.cn#g'

# scp ../dist/js/*.js todo:~/todo/static/js/dist/note-1.0.js

# scp ../dist/css/*.css todo:~/todo/static/css/note-1.0.css
