import { useState } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { EmailAuthService } from '../../services/authService';

const LoginForm = () => {
    const navigate = useNavigate();
    const [email, setEmail] = useState('');
    const [password, setPassword] = useState('');
    const [error, setError] = useState('');
    const [isLoading, setIsLoading] = useState(false);

    const handleSubmit = async (e) => {
        e.preventDefault();
        setIsLoading(true);
        setError('');

        try {
            console.log('로그인 시도:', { email });
            const response = await EmailAuthService.login(email, password);
            console.log('로그인 성공:', response.data);

            // 로그인 성공 시 /app으로 이동
            navigate('/app');
        } catch (error) {
            console.error('로그인 실패:', error);
            console.error('에러 응답:', error.response);

            let errorMessage = '로그인에 실패했습니다.';

            if (error.response?.status === 401) {
                errorMessage = '이메일 또는 비밀번호가 올바르지 않습니다.';
            } else if (error.response?.status === 400) {
                errorMessage = '이메일 인증을 완료해주세요.';
            } else if (error.response?.data?.message) {
                errorMessage = error.response.data.message;
            }

            setError(errorMessage);
        } finally {
            setIsLoading(false);
        }
    };

    return (
        <>
            <form onSubmit={handleSubmit} className="space-y-6">
                <h2 className="text-2xl font-bold text-gray-800 text-center mb-6">
                    이메일로 로그인
                </h2>

                {error && (
                    <div className="bg-red-50 border border-red-200 text-red-700 px-4 py-3 rounded-lg text-sm">
                        {error}
                    </div>
                )}

                <div>
                    <label
                        htmlFor='email'
                        className="block text-sm font-medium text-gray-700 mb-2"
                    >
                        이메일
                    </label>
                    <input
                        id='email'
                        type='email'
                        name='email'
                        value={email}
                        onChange={(e) => setEmail(e.target.value)}
                        required
                        disabled={isLoading}
                        className="w-full px-4 py-3 border border-gray-300 rounded-lg focus:ring-2 focus:ring-sky-500 focus:border-transparent transition-all disabled:bg-gray-100 disabled:cursor-not-allowed"
                        placeholder="example@email.com"
                    />
                </div>

                <div>
                    <label
                        htmlFor='password'
                        className="block text-sm font-medium text-gray-700 mb-2"
                    >
                        비밀번호
                    </label>
                    <input
                        id='password'
                        type='password'
                        name='password'
                        value={password}
                        onChange={(e) => setPassword(e.target.value)}
                        required
                        disabled={isLoading}
                        className="w-full px-4 py-3 border border-gray-300 rounded-lg focus:ring-2 focus:ring-sky-500 focus:border-transparent transition-all disabled:bg-gray-100 disabled:cursor-not-allowed"
                        placeholder="비밀번호를 입력하세요"
                    />
                </div>

                <button
                    type='submit'
                    disabled={isLoading}
                    className="w-full py-3 px-4 bg-sky-500 hover:bg-sky-600 text-white rounded-lg font-medium transition-colors duration-200 shadow-md disabled:bg-gray-400 disabled:cursor-not-allowed"
                >
                    {isLoading ? '로그인 중...' : '로그인'}
                </button>

                <div className="text-center pt-4 border-t border-gray-200">
                    <Link
                        to='/sign-up'
                        className="text-sky-600 hover:text-sky-700 font-medium text-sm"
                    >
                        회원이 아니십니까? 회원가입을 해보세요
                    </Link>
                </div>

                <div className="text-center">
                    <Link
                        to='/'
                        className="text-gray-500 hover:text-gray-700 text-sm"
                    >
                        ← 뒤로 가기
                    </Link>
                </div>
            </form>
        </>
    );
};

export default LoginForm;