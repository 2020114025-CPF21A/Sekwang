
import { useState } from 'react';
import Navigation from '../../components/feature/Navigation.tsx';
import Card from '../../components/base/Card.tsx';
import Button from '../../components/base/Button.tsx';

export default function Attendance() {
  const [attendanceCode, setAttendanceCode] = useState('');
  const [isCheckedIn, setIsCheckedIn] = useState(false);
  const [showQR, setShowQR] = useState(false);

  const handleAttendanceSubmit = () => {
    if (attendanceCode === '1234') {
      setIsCheckedIn(true);
      setAttendanceCode('');
    } else {
      alert('올바른 출석 코드를 입력해주세요.');
    }
  };

  const todayAttendees = [
    { name: '김민수', time: '09:30', status: 'present' },
    { name: '이지은', time: '09:35', status: 'present' },
    { name: '박준호', time: '09:40', status: 'present' },
    { name: '최서연', time: '09:45', status: 'present' },
    { name: '정우진', time: '-', status: 'absent' },
    { name: '한소영', time: '-', status: 'absent' }
  ];

  return (
    <div className="min-h-screen bg-gray-50">
      <Navigation />
      
      <div className="max-w-7xl mx-auto px-4 py-8">
        <div className="text-center mb-8">
          <h1 className="text-3xl font-bold text-gray-800 mb-2">출석 체크</h1>
          <p className="text-gray-600">오늘의 출석을 확인해주세요</p>
        </div>

        <div className="grid grid-cols-1 lg:grid-cols-2 gap-8">
          {/* Attendance Check */}
          <Card title="출석 체크하기">
            {!isCheckedIn ? (
              <div className="space-y-6">
                <div className="text-center">
                  <div className="w-24 h-24 bg-blue-100 rounded-full flex items-center justify-center mx-auto mb-4">
                    <i className="ri-calendar-check-line text-3xl text-blue-600"></i>
                  </div>
                  <h3 className="text-lg font-semibold text-gray-800 mb-2">출석 체크</h3>
                  <p className="text-gray-600 mb-6">관리자가 알려준 코드를 입력하거나 QR코드를 스캔하세요</p>
                </div>

                <div className="space-y-4">
                  <div>
                    <label className="block text-sm font-medium text-gray-700 mb-2">출석 코드</label>
                    <input
                      type="text"
                      value={attendanceCode}
                      onChange={(e) => setAttendanceCode(e.target.value)}
                      placeholder="출석 코드를 입력하세요"
                      className="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-transparent text-sm"
                    />
                  </div>
                  
                  <div className="flex space-x-3">
                    <Button onClick={handleAttendanceSubmit} className="flex-1">
                      출석 체크
                    </Button>
                    <Button onClick={() => setShowQR(!showQR)} variant="secondary">
                      <i className="ri-qr-code-line mr-2"></i>
                      QR 스캔
                    </Button>
                  </div>

                  {showQR && (
                    <div className="text-center p-6 bg-gray-100 rounded-lg">
                      <div className="w-32 h-32 bg-white border-2 border-gray-300 rounded-lg mx-auto mb-4 flex items-center justify-center">
                        <i className="ri-qr-code-line text-4xl text-gray-400"></i>
                      </div>
                      <p className="text-sm text-gray-600">QR 코드 스캔 기능</p>
                    </div>
                  )}
                </div>
              </div>
            ) : (
              <div className="text-center py-8">
                <div className="w-24 h-24 bg-green-100 rounded-full flex items-center justify-center mx-auto mb-4">
                  <i className="ri-check-line text-3xl text-green-600"></i>
                </div>
                <h3 className="text-xl font-bold text-green-600 mb-2">출석 완료!</h3>
                <p className="text-gray-600 mb-4">오늘 출석이 정상적으로 처리되었습니다.</p>
                <div className="bg-green-50 border border-green-200 rounded-lg p-4">
                  <p className="text-sm text-green-700">
                    출석 시간: {new Date().toLocaleTimeString('ko-KR', { hour: '2-digit', minute: '2-digit' })}
                  </p>
                </div>
              </div>
            )}
          </Card>

          {/* Today's Attendance List */}
          <Card title="오늘의 출석 현황">
            <div className="space-y-3">
              <div className="flex justify-between items-center mb-4">
                <span className="text-sm text-gray-600">총 {todayAttendees.length}명 중 {todayAttendees.filter(a => a.status === 'present').length}명 출석</span>
                <span className="text-sm font-semibold text-blue-600">
                  출석률: {Math.round((todayAttendees.filter(a => a.status === 'present').length / todayAttendees.length) * 100)}%
                </span>
              </div>
              
              {todayAttendees.map((attendee, index) => (
                <div key={index} className="flex items-center justify-between p-3 bg-gray-50 rounded-lg">
                  <div className="flex items-center space-x-3">
                    <div className={`w-3 h-3 rounded-full ${attendee.status === 'present' ? 'bg-green-500' : 'bg-gray-300'}`}></div>
                    <span className="font-medium text-gray-800">{attendee.name}</span>
                  </div>
                  <div className="flex items-center space-x-2">
                    {attendee.status === 'present' ? (
                      <>
                        <span className="text-sm text-gray-600">{attendee.time}</span>
                        <i className="ri-check-line text-green-500"></i>
                      </>
                    ) : (
                      <span className="text-sm text-gray-400">미출석</span>
                    )}
                  </div>
                </div>
              ))}
            </div>
          </Card>
        </div>

        {/* Weekly Attendance Summary */}
        <Card title="이번 주 출석 현황" className="mt-8">
          <div className="grid grid-cols-7 gap-2 mb-6">
            {['일', '월', '화', '수', '목', '금', '토'].map((day, index) => (
              <div key={index} className="text-center">
                <div className="text-sm font-medium text-gray-600 mb-2">{day}</div>
                <div className={`w-12 h-12 rounded-full flex items-center justify-center mx-auto ${
                  index < 4 ? 'bg-green-100 text-green-600' : 'bg-gray-100 text-gray-400'
                }`}>
                  {index < 4 ? <i className="ri-check-line"></i> : <i className="ri-close-line"></i>}
                </div>
              </div>
            ))}
          </div>
          
          <div className="text-center">
            <p className="text-lg font-semibold text-gray-800">이번 주 출석: 4일 / 7일</p>
            <p className="text-sm text-gray-600 mt-1">출석률 57%</p>
          </div>
        </Card>
      </div>
    </div>
  );
}
