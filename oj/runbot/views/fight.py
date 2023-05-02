from django.http import JsonResponse
from runbot.views.sandbox import SandBox
from django.views.decorators.csrf import csrf_exempt
import os
import json
import requests
# Create your views here.

def delete_file(code, _input):
    if os.path.isfile(code) and os.path.isfile(_input):
        os.remove(code)
        os.remove(_input)

@csrf_exempt
def fight(request):
  
    body = json.loads(request.body)
    print(body)
    status = body['status'][0]
    container = body['container'][0]
    if status == '2':
        sandbox = SandBox('/root/oj/runbot/code.txt', 'cpp', container, True)
        sandbox.close()
        return JsonResponse({
                'result' : 'ok',
            })

    game = body['game'][0]
    random = body['random'][0]
    path = '/var/lib/docker/overlay2/897480b58ddaaa8548675913f3e23cae745fba0c81fbf56eb41164b9f8bd6ba8/merged/home/acs/kob/backendcloud/' + game + random
   # path = '/var/lib/docker/overlay2/c817665549ba0bd91ca7fa235a6982bcca04343be6273f3f5c800dc2f0e4cbfe/merged/root/kob/backendcloud/' + game + random
    code = path  + 'code.txt'
    _input = path + 'input.txt'

    url = 'http://127.0.0.1:10300/pk/game/' + game + '/receiveBot'

    ext = body['language'][0]
    userId = body['userId'][0]

    if status == '0':
        sandbox = SandBox(code, ext, None, False)
    elif status == '1':
        sandbox = SandBox(code, ext, container, True)

    if sandbox is None:
        delete_file(code, _input)
        return JsonResponse({
                'result': 'fail',
                'msg' : 'create sandbox file',
            })
    if status == '0':
        try:
            sandbox.create()
            sandbox.compile()
            container = sandbox.container
        except:
            delete_file(code, _input)
            sandbox.close()
            raise RuntimeError('sandbox create or complie fail')

    with open(_input,'r',encoding='utf-8') as f:
        content = f.read()
    try:
        d = sandbox.run(content, True)
    except:
        delete_file(code, _input)
        sandbox.close()
        raise

    if d['verdict'] != 'OK':
        sandbox.close()

    print(d)
    if game == 'snake':
        data = {
            "userId" : userId,
            "direction" : d['output'][0],
            "container" : sandbox.container,
        }
    elif game == 'reversi':
        data = {
            "userId" : userId,
            "x" : d['output'][0],
            "y" : d['output'][2],
            "container" : sandbox.container,
        }
    delete_file(code, _input)
    requests.post(url, data)
    return JsonResponse({
        'result' : 'ok',
    })
