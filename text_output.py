import pickle

# 这个脚本把 pkl 文件中存储的信息输出为文本文件，为图存储模块提供输入
#
# 输出
# 1) vertices.txt: 存顶点号以及其对应的 IP 地址
# 格式：（顶点号， IP）
# 2) edges.txt: 边以及边的属性
# 格式：（源顶点号， 目的顶点号， 源端口号， 目的端口号， 次数）
#      （时间序列）
# ps: 时间序列的格式
# [#start
# 每行十个
# #end]

# main

# 把所有的 IP 地址转换成编号
ip_dict = {}
code = 0
f = open('data.pkl', 'rb')
data_dict = pickle.load(f)
f.close()

for key in data_dict:

    if key[0] not in ip_dict:
        code += 1
        ip_dict[key[0]] = code

    if key[1] not in ip_dict:
        code += 1
        ip_dict[key[1]] = code

# 输出到 vertices.txt
fv = open('vertices', 'w')
for key in ip_dict:
    fv.write(str(ip_dict[key]) + ' ' + key + '\n')
fv.close()
print('vertices.txt 输出完毕')

# 输出到 edges.txt
f = open('time.pkl', 'rb')
time_dict = pickle.load(f)
f.close()

fe = open('edges', 'w')

for key in time_dict:
    print('正在输出 ' + str(key))

    # 生成时间序列，并排序
    time_seq = []
    for k in time_dict[key]:
        time_seq.append((k, time_dict[key][k]))
    time_seq.sort()

    try:
        fe.write(str(ip_dict[key[0]]) + ' ')
        fe.write(str(ip_dict[key[1]]) + ' ')
        fe.write(key[2] + ' ' + key[3] + ' ')
        fe.write(str(len(time_seq)) + '\n')
        fe.write('#start\n')
        count = 1
        for t in time_seq:  # 一行10个
            if count >= 10:
                fe.write(str(t) + '\n')
                count = 1
            else:
                fe.write(str(t) + ' ')
                count += 1
        if count <= 10 and count > 1:
            fe.write('\n')
        fe.write('#end\n')

    except KeyError:
        print(key + ' 不存在')

fe.close()
print('edges.txt 输出完毕')
