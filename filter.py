import pickle

# 这个脚本的作用是从流量文件中提取我们需要的数据
#
#
# 输出
# 1) data.pkl 存储dict, key是（s_ip, d_ip, s_port, d_port）vlaue是次数
# 2) time.pkl 存储dict，key是（s_ip, d_ip, s_port, d_port）value是这个连接出现的时间序列的字典


def filter_file(f_name, d, t):
    
    # f_name: 当前分析的文件名
    # d：存data的字典
    # t：存时间序列的字典

    f = open(f_name)

    # 对每一个单独的文件进行分析
    print('现在正在分析', f_name)

    fl = f.readlines()
    link_time = int(fl[0].split()[-1])  # 记录连接的时间
    for j in range(2, len(fl)):

        if '|' not in fl[j]:
            continue

        s = fl[j].split('|')

        # 把大于1023的端口号都记作1024
        try:
            if int(s[2]) > 1023:
                s[2] = '1024'
            if int(s[3]) > 1023:
                s[3] = '1024'
            key = (s[0], s[1], s[2], s[3])  # (s_ip, d_ip, s_port, d_port)
        except IndexError:
            print('IndexError' + ' line:' + str(j))
            print(fl[j])
            continue

        if key in d:
            d[key] += 1
        else:
            d[key] = 1

        if key in t:
            if link_time in t[key]:
                t[key][link_time] += 1
            else:
                t[key][link_time] = 1

        else:
            t[key] = {}
            t[key][link_time] = 1

    f.close()


# main
data_dict = {}
time_dict = {}

# 处理原始文件
for i in range(0, 1753):
    file_name = 'I:\\bh-0110-corsaro\\final-txt-\\' + str(i) + '.txt'
    filter_file(file_name, data_dict, time_dict)

# 输出
    
out = open('data.pkl', 'wb')
pickle.dump(data_dict, out)
out.close()

out = open('time.pkl', 'wb')
pickle.dump(time_dict, out)
out.close()
print('解析完毕, 已正常输出')
