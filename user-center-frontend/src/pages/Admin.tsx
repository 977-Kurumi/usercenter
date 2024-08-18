import '@umijs/max';
import React from 'react';
import {PageContainer} from "@ant-design/pro-components";
import {Outlet} from "umi";

const Admin: React.FC = () => {
  return (
    //使用<Outlet/>后才可正确显示子页面

    <PageContainer content={' 这个页面只有 admin 权限才能查看'}>

      <Outlet/>
    </PageContainer>
  );
};
export default Admin;
