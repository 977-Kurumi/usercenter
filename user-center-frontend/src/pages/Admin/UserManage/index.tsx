import type {ActionType, ProColumns} from '@ant-design/pro-components';
import {ProTable} from '@ant-design/pro-components';
import {Image, message} from 'antd';
import {ReactNode, useRef} from 'react';
import {deleteUser, searchUsers, updateUsers} from "@/services/ant-design-pro/api";

export const waitTimePromise = async (time: number = 100) => {
  return new Promise((resolve) => {
    setTimeout(() => {
      resolve(true);
    }, time);
  });
};



export const waitTime = async (time: number = 100) => {
  await waitTimePromise(time);
};


const columns: ProColumns<API.CurrentUser>[] = [
  {
    width: 48,
    dataIndex: 'id',
    valueType: 'indexBorder',
  },
  {
    title: '用户名',
    dataIndex: 'username',
    copyable: true,//是否允许复制
    ellipsis: true,//是否允许缩略
    tooltip: '用户名过长会自动收缩',
    hideInSearch:true

  },
  {
    title: '用户名',
    dataIndex: 'username',
    copyable: true,//是否允许复制
    ellipsis: true,//是否允许缩略
    hideInTable: true,

  },

  {
    title: '用户账户',
    dataIndex: 'userAccount',
    copyable: true,//是否允许复制
    ellipsis: true,//是否允许缩略
    search: false,
    formItemProps: {
      rules: [
        {
          required: true,
          message: '此项为必填项',
        },
      ],
    },
  },
  {
    title: '用户头像',
    ellipsis: true,//是否允许缩略
    dataIndex: 'avatarUrl',
    search: false,
    render:(dom:ReactNode,record) =>(
      //style={{ textAlign: 'center' }} 设置居中样式
      <div >
        <Image src={record.avatarUrl} width={50}/>
      </div>
    ),
  },
  {
    title: '性别',
    dataIndex: 'gender',
    copyable: true,//是否允许复制
    search: false,
    valueEnum: {
      0: { text: '男' },
      1: {
        text: '女'

      },

    },


  },
  {
    title: '电话',
    dataIndex: 'phone',
    copyable: true,//是否允许复制
    ellipsis: true,//是否允许缩略
    search: false,


  },
  {
    title: '邮箱',
    dataIndex: 'email',
    copyable: true,//是否允许复制
    ellipsis: true,//是否允许缩略
    search: false,

  },
  {
    title: '账号状态',
    dataIndex: 'userStatus',
    search: false,
    valueEnum: {
      0: { text: '正常',status:'Success' },
      1: {
        text: '封禁',
        status: 'error',
      },

    },

  },
  {
    title: '角色',
    dataIndex: 'userRole',
    search: false,
    valueType: 'select',
      filters: true,
      onFilter: true,
      ellipsis: true,
      valueEnum: {
        0: { text: '普通用户',status:'Default' },
        1: {
          text: '管理员',
          status: 'Success',
        },

      },

  },
  {
    title: '创建时间',
    dataIndex: 'createTime',
    valueType: 'dateTime',
    ellipsis: true,//是否允许缩略
    // sorter: true,
    search: false,
    editable:false
  },
  // {
  //   disable: true,
  //   title: '状态',
  //   dataIndex: 'state',
  //   filters: true,
  //   onFilter: true,
  //   ellipsis: true,
  //   valueType: 'select',
  //   valueEnum: {
  //     all: { text: '超长'.repeat(50) },
  //     open: {
  //       text: '未解决',
  //       status: 'Error',
  //     },
  //     closed: {
  //       text: '已解决',
  //       status: 'Success',
  //       disabled: true,
  //     },
  //     processing: {
  //       text: '解决中',
  //       status: 'Processing',
  //     },
  //   },
  // },
  // {
  //   disable: true,
  //   title: '标签',
  //   dataIndex: 'labels',
  //   search: false,
  //   renderFormItem: (_, { defaultRender }) => {
  //     return defaultRender(_);
  //   },
  //   render: (_, record) => (
  //     <Space>
  //       {record.labels.map(({ name, color }) => (
  //         <Tag color={color} key={name}>
  //           {name}
  //         </Tag>
  //       ))}
  //     </Space>
  //   ),
  // },

  {
    title: '操作',
    valueType: 'option',
    key: 'option',
    render: (text, record, _, action) => [
      <a
        key="editable"
        onClick={() => {
          action?.startEditable?.(record.id);
        }}

      >
        编辑
      </a>,
      // <a href={record.url} target="_blank" rel="noopener noreferrer" key="view">
      //   查看
      // </a>,
      // <TableDropdown
      //   key="actionGroup"
      //   onSelect={() => action?.reload()}
      //   menus={[
      //     { key: 'copy', name: '复制' },
      //     { key: 'delete', name: '删除' },
      //   ]}
      // />,
    ],
  },
];


export default () => {
  const actionRef = useRef<ActionType>();
  return (
    <ProTable<API.CurrentUser>
      columns={columns}
      actionRef={actionRef}
      cardBordered
      request={async (params, sort, filter) => {
        console.log(sort, filter);
        const res = await searchUsers({username:params?.username})
        if (res.code===0){
          return {
            data:res.data
          }
        }else {
          return {
            data:[],
          }
        }

      }}
      editable={{
        type: 'multiple',
        //TODO 编辑表格会触发的方法在这里

        onSave: async (key, record) => {
          console.log(key,record)
          const res = await updateUsers(record)
          if (res.code === 0){
            message.success("更新成功");
          }else {
            message.success("更新失败");
          }
        },
        onDelete: async (key, record) => {
          console.log(key,record)
          const res = await deleteUser(record)
          if (res.code === 0){
            message.success("删除成功");
          }else {
            message.success("删除失败");
          }
        }


      }}

      columnsState={{
        persistenceKey: 'pro-table-singe-demos',
        persistenceType: 'localStorage',
        defaultValue: {
          option: { fixed: 'right', disable: true },
        },

      }}
      rowKey="id"
      search={{
        labelWidth: 'auto',
      }}
      options={{
        setting: {
          listsHeight: 400,
        },
      }}
      form={{
        // 由于配置了 transform，提交的参数与定义的不同这里需要转化一下
        syncToUrl: (values, type) => {
          if (type === 'get') {
            return {
              ...values,
              created_at: [values.startTime, values.endTime],
            };
          }
          return values;
        },
      }}
      pagination={{
        pageSize: 5,
        onChange: (page) => console.log(page),
      }}
      dateFormatter="string"
      headerTitle="用户列表"

      toolBarRender={() => [
        // <Button
        //   key="button"
        //   icon={<PlusOutlined />}
        //   onClick={() => {
        //     actionRef.current?.reload();
        //   }}
        //   type="primary"
        // >
        //   新建
        // </Button>
      ]}
    />
  );
};
