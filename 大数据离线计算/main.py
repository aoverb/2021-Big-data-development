from impala.dbapi import connect
from impala.util import as_pandas

'''
host='bigdata118.depts.bingosoft.net'
port=22118
user='user29'
password='pass@bingo29'
database='user29_db'
'''

def impala_conn_exec(sql):
  global host, port, user, password, database
  try:
    conn = connect(host=host, port=int(port), auth_mechanism='PLAIN', user=user, password=password, database=database)
    cursor = conn.cursor()
    cursor.execute(sql)
    data_list=cursor.fetchall()
    return data_list
  except Exception as e:
    return e
	
def load_cfg():
  global host, port, user, password, database
  print("读取配置文件...")
  f = open("config.txt")               # 读取配置
  line = f.readline()               # 调用文件的 readline()方法
  cnt = 0
  while line: 
      cnt = cnt + 1
      if cnt == 1:
          host = line
      elif cnt == 2:
          port = line
      elif cnt == 3:
          user = line
      elif cnt == 4:
          password = line
      elif cnt == 5:
          database = line
      line = f.readline() 
  f.close() 
  print("读取完毕")

def main():
  global host, port, user, password, database
  load_cfg()
  print("欢迎来到SparkSQL查询分析系统。\n")
  while 1:
    cmd = input("请输入SQL命令以执行，或是输入快捷命令以进行快速查询：\n快捷命令：\n1.列出所有数据库\n2.列出表\n3.列出栏\n4.退出\n")
    if cmd == "1":
      print(impala_conn_exec("show databases"))
    elif cmd == "2":
      print(impala_conn_exec("show tables from " + database))
    elif cmd == "3":
      table_name = input("请输入表名:")
      print(impala_conn_exec("show columns from " + table_name))
    elif cmd == "4":
      exit()
    else:
      print(impala_conn_exec(cmd))
    print("\n命令执行完毕。\n")

if __name__ == '__main__':
    main()