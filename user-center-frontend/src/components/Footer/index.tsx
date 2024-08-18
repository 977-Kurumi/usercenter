// import { GithubOutlined } from '@ant-design/icons';
import { DefaultFooter } from '@ant-design/pro-components';
import React from 'react';

const Footer: React.FC = () => {
  const defaulMessage = '977出品';
  const currentYear = new Date().getFullYear();
  return (
    <DefaultFooter

      style={{
        background: 'none',
      }}
      copyright={`${currentYear} ${defaulMessage}`}
      links={[
        {
          key: '',
          title: '',
          href: '',
          blankTarget: true,
        },
        // {
        //   key: '',
        //   title: <GithubOutlined />,
        //   href: '',
        //   blankTarget: true,
        // },
        {
          key: '',
          title: '',
          href: '',
          blankTarget: true,
        },
      ]}
    />
  );
};

export default Footer;
