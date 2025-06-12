import React, { useEffect, useState } from "react";
import { Container, Tabs, Tab, Table, Button, Spinner, Form, OverlayTrigger, Tooltip  } from 'react-bootstrap';
import { useNavigate } from "react-router-dom";
import ApiService from "../../common/Api/ApiService";
import Modal from "../../common/Ui/Modal";
/*
기존의 크기가 큰 컴포넌트 보다는 간단하게 관리할 수 있도록
좀 각색해서 이용할 예정
*/

function ManagementPage(props){

  const [key, setKey] = useState('users');
  const [data, setData] = useState({});
  const [loading, setLoading] = useState(false);
  const [page, setPage] = useState(0);
  const [size, setSize] = useState(10);
  const [totalPage, setTotalPage] = useState(0);
  const [mailModal,setMailModal] = useState(false);
  const [mailTo, setMailTo]= useState('');
  const [mailTitle, setMailTitle]= useState('');
  const [mailContent, setMailContent]= useState('');
  useEffect(() => {
    getData(key);
  }, [key]);

  
  const handleSendMail = async () => {
    const sendMail = {
      userLoginId: mailTo,
      title: mailTitle,
      content: mailContent
    };
    try {
      const response = await ApiService.manage.userSend(sendMail);
      const result = response.data;
      alert(result.message);
    } catch (err) {
      alert("메일 전송 실패");
    } finally {
      setMailModal(false);
      setMailTo('');
      setMailTitle('');
      setMailContent('');
    }
  };

  const getData = async (type) => {
    setLoading(true);
    try{
      let response;
      switch (type) {
        case 'users':
          response = await ApiService.manage.userList(page, size);
          break;
        case 'qnaPosts':
          response = await ApiService.manage.qnaPostList(page, size);
          break;
        case 'qnaAnswers':
          response = await ApiService.manage.qnaAnswerList(page, size);
          break;
        case 'reviews':
          response = await ApiService.manage.reviewList(page, size);
          break;
        case 'reservations':
          response = await ApiService.manage.reservationList(page, size);
          break;
        case 'payments':
          response = await ApiService.manage.paymentList(page, size);
          break;
        default:
          return;
      }

      const result = response.data;
      if (result.result) {
        const keyMap = {
          users: 'userList',
          qnaPosts: 'postList',
          qnaAnswers: 'answerList',
          reviews: 'reviewList',
          reservations: 'reservationList',
          payments: 'payList',
        };
        setData(prev => ({ ...prev, [type]: result[keyMap[type]] }));
        setTotalPage(result.totalPages);
      } else {
        alert(result.message);
        setData(prev => ({ ...prev, [type]: [] }));
      }
    } catch (err) {
      console.error(err);
    }
    setLoading(false);
  };

  const deleteItem = async (type, id) => {
    if (!window.confirm(`${id} 항목을 정말 삭제하시겠습니까?`)) return;
    try{
      let response;
      switch (type) {
        case 'users':
          response = await ApiService.manage.userDelete(id);
          break;
        case 'qnaPosts':
          response = await ApiService.manage.qnaPostDelete(id);
          break;
        case 'qnaAnswers':
          response = await ApiService.manage.qnaAnswerDelete(id);
          break;
        case 'reviews':
          response = await ApiService.manage.reviewDelete(id);
          break;
        default:
          return;
      }

      const result = response.data;
      alert(result.message);
      if (result.result) getData(type);
    }catch(err){
      alert('삭제에 실패했습니다.');
      console.log(err);
    }
  };

  const getDetailData = (type, item)=>{
    switch(type){
      case 'users':
        return (
          <pre>
            <p>아이디: {item.id}</p>
            <p>이름: {item.name}</p>
            <p>로그인 타입: {item.loginType}</p>
            <p>역할: {item.role}</p>
            <p>전화번호: {item.phone}</p>
            <p>생성일: {item.createAt}</p>
            <p>수정일: {item.updateAt}</p>
          </pre>
        );
      case 'qnaPosts':
        return (
          <pre>
            <p>내용: {item.content}</p>
            <p>분류: {item.category}</p>
            <p>조회수: {item.viewCount}</p>
            <p>생성일: {item.createAt}</p>
            <p>수정일: {item.updateAt}</p>
            <p>작성자 아이디: {item.userLoginId}</p>
          </pre>
        );
      case 'qnaAnswers':
        return (
          <pre>
            <p>질문 아이디: {item.postId}</p>
            <p>채택 여부: {item.isAdopted}</p>
            <p>생성일: {item.createAt}</p>
            <p>수정일: {item.updateAt}</p>
            <p>작성자 아이디: {item.userLoginId}</p>
          </pre>
        );
      case 'reviews':
        return (
          <pre>
            <p>예약 아이디: {item.reservationId}</p>
            <p>사업체 이름: {item.petBusinessName}</p>
            <p>별점: {item.rating}</p>
            <p>생성일: {item.createAt}</p>
            <p>수정일: {item.updateAt}</p>
            <p>작성자 아이디: {item.userLoginId}</p>
          </pre>
        );
      case 'reservations':
        return (
          <pre>
            <p>사업체 이름: {item.petBusinessName}</p>
            <p>특별 요구사항: {item.specialRequests}</p>
            <p>사업자 요구사항: {item.businessRequestInfo}</p>
            <p>생성일: {item.createAt}</p>
            <p>수정일: {item.updateAt}</p>
            <p>예약자 아이디: {item.userLoginId}</p>
          </pre>
        );
      case 'payments':
        return(
          <pre>
            <p>impUid: {item.impUid}</p>
            <p>서비스 요금: {item.serviceFee}</p>
            <p>결제 방법: {item.paymentMethod}</p>
            <p>결제 시간: {item.transactionTime}</p>
          </pre>
        );
      default:
        return;
    }
  };

  const renderTable = (type, list, columns, deletable = false) => (
    <Table striped bordered hover>
      <thead>
        <tr>
          {columns.map(col => <th key={col}>{col}</th>)}
          {deletable && <th>작업</th>}
          {type === 'users' && <th>메일 전송</th>}
        </tr>
      </thead>
      <tbody>
        {list && list.length > 0 ? list.map((item, idx) => (
        <OverlayTrigger
          key={idx}
          placement="top"
          overlay={
            <Tooltip id={`tooltip-${idx}`}>
              <pre style={{ margin: 0, whiteSpace: 'pre-wrap' }}>
                {getDetailData(type, item)}
              </pre>
            </Tooltip>
          }
        >
          <tr>
            {columns.map(col => (
              <td key={col}>{item[col]}</td>
            ))}
            {deletable && (
              <td>
                { type ==='users'?
                <Button variant="danger" size="sm"
                  onClick={() => deleteItem(type, item.userLoginId)}>
                  강제 삭제
                </Button>
                :
                <Button variant="danger" size="sm"
                  onClick={() => deleteItem(type, item.id)}>
                  강제 삭제
                </Button>
                }    
              </td>
            )}
            {type === 'users' && (
              <td>
                <Button variant="primary" size="sm" onClick={() => {
                  setMailModal(true);
                  setMailTo(item.userLoginId);
                }}>
                  메일 전송
                </Button>
              </td>
            )}
          </tr>
        </OverlayTrigger>
        )) : (
          <tr>
            <td colSpan={columns.length + (deletable ? 1 : 0)}>데이터 없음</td>
          </tr>
        )}
      </tbody>
    </Table>
  );

  return (
    <Container className="mt-4">
      <h3>관리자 페이지</h3>
      <Tabs activeKey={key} onSelect={(k) => setKey(k)} className="mb-3" justify>
        <Tab eventKey="users" title="유저">
          {loading ? <Spinner animation="border" /> :
            renderTable('users', data.users || [], ['userLoginId', 'email'], true)}
        </Tab>
        <Tab eventKey="qnaPosts" title="QnA 질문">
          {loading ? <Spinner animation="border" /> :
            renderTable('qnaPosts', data.qnaPosts || [], ['id', 'title'], true)}
        </Tab>
        <Tab eventKey="qnaAnswers" title="QnA 답변">
          {loading ? <Spinner animation="border" /> :
            renderTable('qnaAnswers', data.qnaAnswers || [], ['id', 'content'], true)}
        </Tab>
        <Tab eventKey="reviews" title="리뷰">
          {loading ? <Spinner animation="border" /> :
            renderTable('reviews', data.reviews || [], ['id', 'content'], true)}
        </Tab>
        <Tab eventKey="reservations" title="예약">
          {loading ? <Spinner animation="border" /> :
            renderTable('reservations', data.reservations || [], ['id', 'status', 'checkIn', 'checkOut'])}
        </Tab>
        <Tab eventKey="payments" title="결제">
          {loading ? <Spinner animation="border" /> :
            renderTable('payments', data.payments || [], ['id', 'reservationId', 'amount', 'paymentStatus', 'refundAmount'])}
        </Tab>
      </Tabs>

      
      <Modal isOpen={mailModal} onClose={() => setMailModal(false)}>
        <h2>메일 전송</h2>
        <Form>
          <Form.Group className="mb-3">
            <Form.Label>받는 유저</Form.Label>
            <Form.Control type="text" value={mailTo} readOnly />
          </Form.Group>
          <Form.Group className="mb-3">
            <Form.Label>제목</Form.Label>
            <Form.Control type="text" value={mailTitle} onChange={(e) => setMailTitle(e.target.value)} />
          </Form.Group>
          <Form.Group className="mb-3">
            <Form.Label>내용</Form.Label>
            <Form.Control as="textarea" rows={4} value={mailContent} onChange={(e) => setMailContent(e.target.value)} />
          </Form.Group>
          <Button variant="primary" onClick={handleSendMail}>전송</Button>
        </Form>
      </Modal>
    </Container>
  );
}

export default ManagementPage;