<%--
  Created by IntelliJ IDEA.
  User: 聆听
  Date: 2022/3/31
  Time: 16:14
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %><%
    String basePath = request.getScheme() + "://" +request.getServerName() + ":"+
            request.getServerPort()+request.getContextPath()+"/";
%>
<html>
<head>
    <base href="<%=basePath%>">
    <title>Title</title>
    <script src="ECharts/echarts.min.js"></script>
    <script src="jquery/jquery-1.11.1-min.js"></script>
</head>

    <script>
        $(function () {
            //页面加载完成之后绘制统计图表
            CharTransaction();
        })
        function CharTransaction() {

            $.ajax({
                url:"workbench/transaction/getChars.do",
                data:"",
                type:"get",
                dataType:"json",
                success:function (data) {
// 基于准备好的dom，初始化echarts实例
                    var myChart = echarts.init(document.getElementById('main'));
                    // 指定图表的配置项和数据
                    option = {
                        title: {
                            text: '交易统计图'
                        },
                        tooltip: {
                            trigger: 'item',
                            formatter: '{a} <br/>{b} : {c}%'
                        },
                        toolbox: {
                            feature: {
                                dataView: { readOnly: false },
                                restore: {},
                                saveAsImage: {}
                            }
                        },
                        legend: {
                            data: ['Show', 'Click', 'Visit', 'Inquiry', 'Order']
                        },
                        series: [
                            {
                                name: 'Funnel',
                                type: 'funnel',
                                left: '10%',
                                top: 60,
                                bottom: 60,
                                width: '80%',
                                min: 0,
                                max: data.total,
                                minSize: '0%',
                                maxSize: '100%',
                                sort: 'descending',
                                gap: 2,
                                label: {
                                    show: true,
                                    position: 'inside'
                                },
                                labelLine: {
                                    length: 10,
                                    lineStyle: {
                                        width: 1,
                                        type: 'solid'
                                    }
                                },
                                itemStyle: {
                                    borderColor: '#fff',
                                    borderWidth: 1
                                },
                                emphasis: {
                                    label: {
                                        fontSize: 20
                                    }
                                },
                                data:data.arrayList
                                /*[
                                    { value: 60, name: 'Visit' },
                                    { value: 40, name: 'Inquiry' },
                                    { value: 20, name: 'Order' },
                                    { value: 80, name: 'Click' },
                                    { value: 100, name: 'Show' }
                                ]*/
                            }
                        ]
                    };
            // 使用刚指定的配置项和数据显示图表。
                    myChart.setOption(option);
                }
            })




        }
    </script>

<body>

    <div id="main" style="width: 600px;height:400px;"></div>

</body>
</html>
